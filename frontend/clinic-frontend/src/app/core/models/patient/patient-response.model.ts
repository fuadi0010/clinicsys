export interface PatientResponse {
  id: number;
  userId: number;
  fullName: string;
  gender: string;
  birthDate: string;
  phoneNumber: string;
  address: string;
  bloodType: string;
  profileImageUrl: string | null;
  active?: boolean;
  medicalRecordNumber?: string;
  identityNumber?: string;
  allergyNotes?: string;
  insuranceNumber?: string;
  medicalNotes?: string;
}