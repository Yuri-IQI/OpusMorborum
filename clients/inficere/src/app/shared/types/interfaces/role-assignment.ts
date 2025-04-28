export interface RoleAssignment {
  id: number;
  signedRegistration: {id: number, signature: string, apothecaryAvailable: boolean, herbalistAvailable: boolean};
  roleName: 'APOTHECARY' | 'HERBALIST';
}
