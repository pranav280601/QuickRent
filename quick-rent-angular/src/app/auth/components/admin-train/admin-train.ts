
import { RecommendationService } from '../services/recommendation.service';
import { Component } from '@angular/core';

@Component({
  selector: 'app-admin-train',
  standalone: false,
  templateUrl: './admin-train.html',
  styleUrl: './admin-train.scss'
})

export class AdminTrain {

    busy = false;
    message = '';


    constructor(private recService: RecommendationService) {}


    triggerTrain() {
    this.busy = true;
    this.message = '';
    this.recService.triggerRetrain().subscribe({
    next: (res :any) => {
    this.message = 'Retrain triggered successfully';
    this.busy = false;
    },
    error: (err :any) => {
    console.error(err);
    this.message = 'Retrain failed';
    this.busy = false;
    }
    });
  }
}
