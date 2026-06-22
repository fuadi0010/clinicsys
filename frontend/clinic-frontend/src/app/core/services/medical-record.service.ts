import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { environment } from '../../../environments/environment';
import { MedicalRecordResponse } from '../models/medical-record/medical-record-response.model';
import { CreateMedicalRecordRequest } from '../models/medical-record/create-medical-record-request.model';
import { UpdateMedicalRecordRequest } from '../models/medical-record/update-medical-record-request.model';
import { ApiResponse } from '../models/api-response.model';

@Injectable({
  providedIn: 'root'
})
export class MedicalRecordService {
  private readonly http = inject(HttpClient);
  private readonly API = `${environment.apiUrl}/api/medical-records`;

  createMedicalRecord(request: CreateMedicalRecordRequest): Observable<MedicalRecordResponse> {
    return this.http.post<ApiResponse<MedicalRecordResponse>>(`${this.API}/create`, request).pipe(
      map(res => res.data)
    );
  }

  getDoctorMedicalRecords(): Observable<MedicalRecordResponse[]> {
    return this.http.get<ApiResponse<MedicalRecordResponse[]>>(`${this.API}/doctor/me`).pipe(
      map(res => res.data)
    );
  }

  getPatientMedicalRecords(): Observable<MedicalRecordResponse[]> {
    return this.http.get<ApiResponse<MedicalRecordResponse[]>>(`${this.API}/me`).pipe(
      map(res => res.data)
    );
  }

  updateMedicalRecord(id: number, request: UpdateMedicalRecordRequest): Observable<MedicalRecordResponse> {
    return this.http.patch<ApiResponse<MedicalRecordResponse>>(`${this.API}/${id}`, request).pipe(
      map(res => res.data)
    );
  }
}