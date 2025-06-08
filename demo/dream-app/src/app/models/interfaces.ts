// Updated interfaces for the application

export interface Person {
  id?: number;
  meno: string;
  priezvisko: string;
  datumNarodenia: string;
}

export interface Vaccine {
  id?: number;
  nazov: string;
  typ: string;
  vyrobca: string; // Added missing field used in AddVaccineComponent
  pocetDavok?: number;
}

export interface Vaccination {
  id?: number;
  osobaId: number | null;
  vakcinaId: number | null;
  datumAplikacie: string | Date;
  poradieDavky: number;
}

export interface VaccinationRecord {
  id: number;
  osobaId?: number;
  vakcinaId?: number;
  menoOsoby: string;
  priezviskoOsoby: string;
  nazovVakciny: string;
  datumVakciny: string;
  aktualnaDavka: number;
  potrebnaDavka: number;
  emailOsoby?: string;
  
  // Pridané pre backward compatibility s template
  osobaMeno: string;
  osobaPriezvisko: string;
  vakcinaNazov: string;
  vakcinaTyp?: string;
  datumAplikacie: string;
  poradieDavky: number;
}