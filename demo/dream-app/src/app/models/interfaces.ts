// src/app/models/interfaces.ts

export interface Person {
  id?: number;
  meno: string;
  priezvisko: string;
  datumNarodenia?: string;
  rokNarodenia?: number;
  pohlavie?: string;
}

export interface Vaccine {
  id?: number;
  nazov: string;
  typ: string;
  vyrobca: string;
}

export interface Vaccination {
  id?: number;
  osobaId: number;
  vakcinaId: number;
  datumAplikacie: string;
  poradieDavky: number;
}

// Add this interface for the vaccinated person records
export interface VaccinationRecord {
  id?: number;
  osobaId: number;
  vakcinaId: number;
  datumAplikacie: string;
  poradieDavky: number;
  osobaMeno: string;
  osobaPriezvisko: string;
  vakcinaNazov: string;
  vakcinaTyp: string;
}// src/app/models/interfaces.ts

export interface Person {
  id?: number;
  meno: string;
  priezvisko: string;
  datumNarodenia?: string;
  rokNarodenia?: number;
  pohlavie?: string;
}

export interface Vaccine {
  id?: number;
  nazov: string;
  typ: string;
  vyrobca: string;
}

export interface Vaccination {
  id?: number;
  osobaId: number;
  vakcinaId: number;
  datumAplikacie: string;
  poradieDavky: number;
}

// Add this interface for the vaccinated person records
export interface VaccinationRecord {
  id?: number;
  osobaId: number;
  vakcinaId: number;
  datumAplikacie: string;
  poradieDavky: number;
  osobaMeno: string;
  osobaPriezvisko: string;
  vakcinaNazov: string;
  vakcinaTyp: string;
}