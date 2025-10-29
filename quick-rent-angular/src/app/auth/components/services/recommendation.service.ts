import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environments';

@Injectable({
  providedIn: 'root'
})
export class RecommendationService {
  private baseUrl = environment.apiUrl; // e.g. http://localhost:8080

  constructor(private http: HttpClient) {}

  // Fetch recommendations for the logged-in user (by userId)
  getRecommendations(userId: number): Observable<any> {
    return this.http.get(`${this.baseUrl}/api/recommendations/${userId}`);
  }

  // Optional: trigger ML retraining (admin-only)
  triggerRetrain(): Observable<any> {
    return this.http.post(`${this.baseUrl}/api/ml/train`, {});
  }
}