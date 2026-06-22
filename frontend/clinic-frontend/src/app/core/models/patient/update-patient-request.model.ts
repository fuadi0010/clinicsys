export interface UpdatePatientRequest {
  fullName: string;
  gender: string;
  birthDate: string;
  phoneNumber: string;
  address: string;
  bloodType: string;
  identityNumber?: string;
  allergyNotes?: string;
  insuranceNumber?: string;
  medicalNotes?: string;
}