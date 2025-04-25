import { Component, Input } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators, AbstractControl, ValidationErrors } from '@angular/forms';
import { RequestLevelEnum, RequestLevelEnumNameMap } from '../../types/enums/request-level-enum';
import { CustomerRequest } from '../../types/interfaces/customer-request';
import { CommonModule } from '@angular/common';
import { response } from 'express';

@Component({
  selector: 'app-requests-creation',
  imports: [ReactiveFormsModule, CommonModule],
  standalone: true,
  templateUrl: './requests-creation.component.html',
  styleUrl: './requests-creation.component.css'
})
export class RequestsCreationComponent {
  form!: FormGroup;
  jsonForm!: FormGroup;
  requestLevels = Object.values(RequestLevelEnum).filter(value => typeof value === 'number') as number[];
  @Input() voxAegriUrl!: string;
  selectedMode: string = 'FORM';

  constructor(private fb: FormBuilder) {}

  ngOnInit() {
    this.form = this.fb.group({
      customerRequestId: [null],
      title: ['', Validators.required],
      description: ['', Validators.required],
      level: [null, Validators.required],
      baseReward: [0, [Validators.required, Validators.min(0)]],
      isActive: [true],
      author: ['', Validators.required],
    });

    this.jsonForm = this.fb.group({
      jsonInput: ['', [Validators.required, this.jsonValidator()]]
    });
  }

  jsonValidator() {
    return (control: AbstractControl): ValidationErrors | null => {
      try {
        const parsed = JSON.parse(control.value);
        if (!Array.isArray(parsed)) return { invalidJson: true };
        return null;
      } catch {
        return { invalidJson: true };
      }
    };
  }

  onSubmit() {
    if (this.form.valid) {
      const requestBody: CustomerRequest = {
        ...this.form.value,
        level: RequestLevelEnum[this.form.value.level],
        baseReward: parseFloat(this.form.value.baseReward)
      };
      this.createQuest(requestBody);
    }
  }

  onSubmitJson() {
    if (this.jsonForm.valid) {
      try {
        const jsonInput = this.jsonForm.value.jsonInput;
        const parsedRequests = JSON.parse(jsonInput) as CustomerRequest[];

        parsedRequests.forEach(request => {
          const cleanedRequest: CustomerRequest = {
            ...request,
            level: request.level,
            baseReward: parseFloat(request.baseReward as any)
          };
          this.createQuest(cleanedRequest);
        });

        this.jsonForm.reset();
      } catch (e) {
        console.error('Invalid JSON structure:', e);
      }
    }
  }

  private async createRequest(endpoint: string, method: 'POST' | 'GET' | 'PUT' | 'DELETE', reqBody: any | null = null) {
    const url = `${this.voxAegriUrl}${endpoint}`;
    const options: RequestInit = {
      method,
      headers: { 'Content-Type': 'application/json' },
      ...(reqBody ? { body: JSON.stringify(reqBody) } : {})
    };

    try {
      const response = await fetch(url, options);
      if (!response.ok) {
        throw new Error(`Request to ${url} failed with status ${response.status}`);
      }
      return await response.json();
    } catch (error) {
      console.error(`Error during ${method} ${endpoint}:`, error);
      throw error;
    }
  }

  createQuest(requestBody: CustomerRequest) {
    this.createRequest('/customer-request', 'POST', requestBody)
      .then(data => {
        console.log('Single request created:', data);
        this.form.reset();
      });
  }

  createListOfQuests(requestList: CustomerRequest[]) {
    this.createRequest('/customer-request/create-many', 'POST', requestList)
      .then(data => {
        console.log('Batch request created:', data);
        this.jsonForm.reset();
      });
  }

  selectMode(mode: string) {
    this.selectedMode = mode;
  }
}
