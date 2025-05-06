// Predpokladám, že tento súbor by mal byť v src/app/models/interfaces.ts

export interface Person {
  id: number;
  meno: string;
  priezvisko: string;
  rodCislo?: string;
  datumNarodenia?: string;
  ulica?: string;
  cislo?: string;
  mesto?: string;
  psc?: string;
}

export interface Vaccine {
  id: number;
  nazov: string;
  typ: string;
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
  osobaId: number;
  vakcinaId: number;
  datumAplikacie: string;
  poradieDavky: number;
  osobaMeno: string;
  osobaPriezvisko: string;
  vakcinaNazov: string;
  vakcinaTyp: string;
}