export interface MedicalRecordResponse {

  id: number;

  patientId: number;

  doctorId: number;

  appointmentId: number;

  complaint: string;

  diagnosis: string;

  treatment: string;

  notes: string;

  examinationDate: string;

}