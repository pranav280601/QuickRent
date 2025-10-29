import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminTrain } from './admin-train';

describe('AdminTrain', () => {
  let component: AdminTrain;
  let fixture: ComponentFixture<AdminTrain>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AdminTrain]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminTrain);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
