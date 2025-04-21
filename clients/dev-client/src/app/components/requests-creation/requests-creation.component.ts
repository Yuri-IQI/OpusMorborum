import { Component, Input } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { RequestLevelEnum } from '../../types/enums/request-level-enum';
import { CustomerRequest } from '../../types/interfaces/customer-request';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-requests-creation',
  imports: [ReactiveFormsModule, CommonModule],
  standalone: true,
  templateUrl: './requests-creation.component.html',
  styleUrl: './requests-creation.component.css'
})
export class RequestsCreationComponent {
  form!: FormGroup;
  requestLevels = Object.values(RequestLevelEnum).filter(value => typeof value === 'number') as number[];
  @Input() voxAegriUrl!: string;

  constructor(private fb: FormBuilder) {}

  ngOnInit() {
    this.form = this.fb.group({
      customerRequestId: [null],
      title: ['', Validators.required],
      description: ['', Validators.required],
      level: [null, Validators.required],
      baseReward: [0, [Validators.required, Validators.min(0)]],
      isActive: [true],
      author: ['', Validators.required]
    });
  }

  onSubmit() {
    if (this.form.valid) {
      const requestBody = {
        ...this.form.value,
        level: RequestLevelEnum[this.form.value.level],
        baseReward: parseFloat(this.form.value.baseReward)
      };

      console.log('Customer Request:', requestBody);
      this.createQuest(requestBody);
    }
  }

  createQuest(requestBody: CustomerRequest) {
    const url = `${this.voxAegriUrl}/customer-request`;
    const options = {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(requestBody)
    };

    fetch(url, options)
      .then(response => {
        if (!response.ok) {
          throw new Error('Network response was not ok');
        }
        return response.json();
      })
      .then(data => {
        console.log('Success:', data);
        this.form.reset();
      })
      .catch(error => {
        console.error('Error:', error);
      });
  }
}
