export interface Person {
  id?: number;
  meno: string;
  priezvisko: string;
  datumNarodenia: string;  // formát YYYY-MM-DD
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
