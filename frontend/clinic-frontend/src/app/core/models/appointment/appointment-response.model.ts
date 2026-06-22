export interface AppointmentResponse {

  id: number;

  patientId: number;

  patientUserId: number;

  doctorId: number;

  doctorUserId: number;

  appointmentDate: string;

  appointmentTime: string;

  status: string;

  notes: string;

  createdAt: string;

  paymentQrUrl?: string;

  hasMedicalRecord?: boolean;

}