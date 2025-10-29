import { Component, OnInit } from '@angular/core'
import { RecommendationService } from '../services/recommendation.service'

@Component({
  selector: 'app-recommendations',
  standalone: false,
  templateUrl: './recommendations.html',
  styleUrl: './recommendations.scss'
})

export class Recommendations implements OnInit{
  recommendedCars: any[] = [];
  loading = false;
  error = '';

  constructor(private recService: RecommendationService) {}

  ngOnInit(): void {
    this.loadRecommendations();
  }

  loadRecommendations(): void {
    const userIdStr = localStorage.getItem('userId');
    if (!userIdStr) {
      this.error = 'User not logged in';
      return;
    }

    const userId = Number(userIdStr);
    this.loading = true;
    this.recService.getRecommendations(userId).subscribe({
      next: (res: any) => {
        // response shape depends on backend; handle both ML and rule-based
        // If response is list of Car objects
        if (Array.isArray(res)) {
          this.recommendedCars = res;
        } else if (res.recommendations) {
          // If backend returns { recommendations: [carIds] }
          // Ideally backend returns full Car objects; if only IDs, make additional call (not included)
          // We'll handle both: if elements are objects, use them; if numbers, leave as IDs
          const first = res.recommendations[0];
          if (typeof first === 'object') {
            this.recommendedCars = res.recommendations;
          } else {
            // IDs returned: you might want to fetch details from /api/cars/{id}
            // For now, map IDs into a placeholder structure
            this.recommendedCars = res.recommendations.map((id: number) => ({ id, name: 'Car #' + id }));
          }
        } else {
          this.recommendedCars = [];
        }
        this.loading = false;
      },
      error: (err : any) => {
        this.error = 'Failed to load recommendations';
        console.error(err);
        this.loading = false;
      }
    });
  }
}
