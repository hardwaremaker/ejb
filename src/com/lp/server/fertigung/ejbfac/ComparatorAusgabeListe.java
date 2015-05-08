/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2015 HELIUM V IT-Solutions GmbH
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published 
 * by the Free Software Foundation, either version 3 of theLicense, or 
 * (at your option) any later version.
 * 
 * According to sec. 7 of the GNU Affero General Public License, version 3, 
 * the terms of the AGPL are supplemented with the following terms:
 * 
 * "HELIUM V" and "HELIUM 5" are registered trademarks of 
 * HELIUM V IT-Solutions GmbH. The licensing of the program under the 
 * AGPL does not imply a trademark license. Therefore any rights, title and
 * interest in our trademarks remain entirely with us. If you want to propagate
 * modified versions of the Program under the name "HELIUM V" or "HELIUM 5",
 * you may only do so if you have a written permission by HELIUM V IT-Solutions 
 * GmbH (to acquire a permission please contact HELIUM V IT-Solutions
 * at trademark@heliumv.com).
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contact: developers@heliumv.com
 ******************************************************************************/
package com.lp.server.fertigung.ejbfac;

import java.util.Comparator;

import com.lp.server.fertigung.service.ReportLosAusgabelisteDto;
import com.lp.util.Helper;

/**
 * <p>
 * Diese Klasse kuemmert sich um die Sortierung der aufloesbaren Fehlmengen
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2007
 * </p>
 * 
 * <p>
 * Erstellung: Martin Bluehweis; 19.01.06
 * </p>
 * 
 * <p>
 * 
 * @author $Author: christian $
 *         </p>
 * 
 * @version not attributable Date $Date: 2011/03/01 11:17:45 $
 */
public class ComparatorAusgabeListe implements Comparator<Object> {

	private int iSortierungNachWas = -1;
	private boolean bVorrangigNachFarbcode = false;

	public ComparatorAusgabeListe(int iSortierungNachWasI,
			boolean bVorrangigNachFarbcode) {
		iSortierungNachWas = iSortierungNachWasI;
		this.bVorrangigNachFarbcode = bVorrangigNachFarbcode;
	}

	public int compare(Object a, Object b) {
		ReportLosAusgabelisteDto afmDto1 = (ReportLosAusgabelisteDto) a;
		ReportLosAusgabelisteDto afmDto2 = (ReportLosAusgabelisteDto) b;
		switch (iSortierungNachWas) {
		case Helper.SORTIERUNG_NACH_IDENT: {
			if (bVorrangigNachFarbcode == true) {
				int i = afmDto1.getSFarbcode()
						.compareTo(afmDto2.getSFarbcode());

				if (i == 0) {

					if (afmDto1 != null && afmDto1.getSIdent() != null
							&& afmDto2 != null && afmDto2.getSIdent() != null) {
						return afmDto1.getSIdent().compareTo(
								afmDto2.getSIdent());
					} else {
						return 0;
					}
				} else {
					return i;
				}
			} else {
				if (afmDto1 != null && afmDto1.getSIdent() != null
						&& afmDto2 != null && afmDto2.getSIdent() != null) {
					return afmDto1.getSIdent().compareTo(afmDto2.getSIdent());
				} else {
					return 0;
				}
			}

		}
		case Helper.SORTIERUNG_NACH_ARTIKELBEZEICHNUNG: {
			if (bVorrangigNachFarbcode == true) {
				int i = afmDto1.getSFarbcode()
						.compareTo(afmDto2.getSFarbcode());

				if (i == 0) {

					if (afmDto1 != null && afmDto1.getSBezeichnung() != null
							&& afmDto2 != null && afmDto2.getSBezeichnung() != null) {
						return afmDto1.getSBezeichnung().compareTo(
								afmDto2.getSBezeichnung());
					} else {
						return 0;
					}
				} else {
					return i;
				}
			} else {
				if (afmDto1 != null && afmDto1.getSBezeichnung() != null
						&& afmDto2 != null && afmDto2.getSBezeichnung() != null) {
					return afmDto1.getSBezeichnung().compareTo(afmDto2.getSBezeichnung());
				} else {
					return 0;
				}
			}

		}
		case Helper.SORTIERUNG_NACH_LAGER_UND_LAGERORT: {

			if (bVorrangigNachFarbcode == true) {
				int j = afmDto1.getSFarbcode()
						.compareTo(afmDto2.getSFarbcode());

				if (j == 0) {

					if (afmDto1 != null && afmDto1.getSLager() != null
							&& afmDto2 != null && afmDto2.getSLager() != null) {
						int i = afmDto1.getSLager().compareTo(
								afmDto2.getSLager());
						if (i == 0) {
							if (afmDto1 != null
									&& afmDto1.getSLagerort() != null
									&& afmDto2 != null
									&& afmDto2.getSLagerort() != null) {
								return afmDto1.getSLagerort().compareTo(
										afmDto2.getSLagerort());
							} else {
								return 0;
							}
						} else {
							return i;
						}
					} else {
						return 0;
					}
				} else {
					return j;
				}
			} else {

				if (afmDto1 != null && afmDto1.getSLager() != null
						&& afmDto2 != null && afmDto2.getSLager() != null) {
					int i = afmDto1.getSLager().compareTo(afmDto2.getSLager());
					if (i == 0) {
						if (afmDto1 != null && afmDto1.getSLagerort() != null
								&& afmDto2 != null
								&& afmDto2.getSLagerort() != null) {
							return afmDto1.getSLagerort().compareTo(
									afmDto2.getSLagerort());
						} else {
							return 0;
						}
					} else {
						return i;
					}
				} else {
					return 0;
				}
			}

		}
		case Helper.SORTIERUNG_NACH_LAGERORT_UND_LAGER: {

			if (bVorrangigNachFarbcode == true) {
				int j = afmDto1.getSFarbcode()
						.compareTo(afmDto2.getSFarbcode());

				if (j == 0) {
					if (afmDto1 != null && afmDto1.getSLagerort() != null
							&& afmDto2 != null
							&& afmDto2.getSLagerort() != null) {
						int i = afmDto1.getSLagerort().compareTo(
								afmDto2.getSLagerort());
						if (i == 0) {
							if (afmDto1 != null && afmDto1.getSLager() != null
									&& afmDto2 != null
									&& afmDto2.getSLager() != null) {
								return afmDto1.getSLager().compareTo(
										afmDto2.getSLager());
							} else {
								return 0;
							}
						} else {
							return i;
						}
					} else {
						return 0;
					}
				} else {
					return j;
				}
			} else {
				if (bVorrangigNachFarbcode == true) {
					int j = afmDto1.getSFarbcode().compareTo(
							afmDto2.getSFarbcode());

					if (j == 0) {
						if (afmDto1 != null && afmDto1.getSLagerort() != null
								&& afmDto2 != null
								&& afmDto2.getSLagerort() != null) {
							int i = afmDto1.getSLagerort().compareTo(
									afmDto2.getSLagerort());
							if (i == 0) {
								if (afmDto1 != null
										&& afmDto1.getSLager() != null
										&& afmDto2 != null
										&& afmDto2.getSLager() != null) {
									return afmDto1.getSLager().compareTo(
											afmDto2.getSLager());
								} else {
									return 0;
								}
							} else {
								return i;
							}
						} else {
							return 0;
						}
					} else {
						return j;
					}
				} else {

					if (bVorrangigNachFarbcode == true) {
						int j = afmDto1.getSFarbcode().compareTo(
								afmDto2.getSFarbcode());

						if (j == 0) {
							if (afmDto1 != null
									&& afmDto1.getSLagerort() != null
									&& afmDto2 != null
									&& afmDto2.getSLagerort() != null) {
								int i = afmDto1.getSLagerort().compareTo(
										afmDto2.getSLagerort());
								if (i == 0) {
									if (afmDto1 != null
											&& afmDto1.getSLager() != null
											&& afmDto2 != null
											&& afmDto2.getSLager() != null) {
										return afmDto1.getSLager().compareTo(
												afmDto2.getSLager());
									} else {
										return 0;
									}
								} else {
									return i;
								}
							} else {
								return 0;
							}
						} else {
							return j;
						}
					} else {
						if (afmDto1 != null && afmDto1.getSLagerort() != null
								&& afmDto2 != null
								&& afmDto2.getSLagerort() != null) {
							int i = afmDto1.getSLagerort().compareTo(
									afmDto2.getSLagerort());
							if (i == 0) {
								if (afmDto1 != null
										&& afmDto1.getSLager() != null
										&& afmDto2 != null
										&& afmDto2.getSLager() != null) {
									return afmDto1.getSLager().compareTo(
											afmDto2.getSLager());
								} else {
									return 0;
								}
							} else {
								return i;
							}
						} else {
							return 0;
						}
					}

				}

			}

		}
		case Helper.SORTIERUNG_NACH_MONTAGEART_UND_SCHALE: {

			if (bVorrangigNachFarbcode == true) {
				int j = afmDto1.getSFarbcode()
						.compareTo(afmDto2.getSFarbcode());

				if (j == 0) {
					if (afmDto1 != null && afmDto1.getSMontageart() != null
							&& afmDto2 != null
							&& afmDto2.getSMontageart() != null) {
						int i = afmDto1.getSMontageart().compareTo(
								afmDto2.getSMontageart());
						if (i == 0) {
							if (afmDto1 != null && afmDto1.getISchale() != null
									&& afmDto2 != null
									&& afmDto2.getISchale() != null) {
								return afmDto1.getISchale().compareTo(
										afmDto2.getISchale());
							} else {
								return 0;
							}
						} else {
							return i;
						}
					} else {
						return 0;
					}
				} else {
					return j;
				}
			} else {
				if (afmDto1 != null && afmDto1.getSMontageart() != null
						&& afmDto2 != null && afmDto2.getSMontageart() != null) {
					int i = afmDto1.getSMontageart().compareTo(
							afmDto2.getSMontageart());
					if (i == 0) {
						if (afmDto1 != null && afmDto1.getISchale() != null
								&& afmDto2 != null
								&& afmDto2.getISchale() != null) {
							return afmDto1.getISchale().compareTo(
									afmDto2.getISchale());
						} else {
							return 0;
						}
					} else {
						return i;
					}
				} else {
					return 0;
				}
			}

		}
		case Helper.SORTIERUNG_NACH_ARTIKELKLASSE: {
			if (bVorrangigNachFarbcode == true) {
				int j = afmDto1.getSFarbcode()
						.compareTo(afmDto2.getSFarbcode());

				if (j == 0) {
					if (afmDto1 != null && afmDto1.getSArtikelklasse() != null
							&& afmDto2 != null
							&& afmDto2.getSArtikelklasse() != null) {
						return afmDto1.getSArtikelklasse().compareTo(
								afmDto2.getSArtikelklasse());
					} else {
						return 0;
					}
				} else {
					return j;
				}
			} else {
				if (afmDto1 != null && afmDto1.getSArtikelklasse() != null
						&& afmDto2 != null
						&& afmDto2.getSArtikelklasse() != null) {
					return afmDto1.getSArtikelklasse().compareTo(
							afmDto2.getSArtikelklasse());
				} else {
					return 0;
				}
			}

		}
		case Helper.SORTIERUNG_NACH_ARTIKELKLASSE_UND_MATERIAL: {
			if (bVorrangigNachFarbcode == true) {
				int j = afmDto1.getSFarbcode()
						.compareTo(afmDto2.getSFarbcode());

				if (j == 0) {
					if (afmDto1 != null && afmDto1.getSArtikelklasse() != null
							&& afmDto2 != null
							&& afmDto2.getSArtikelklasse() != null) {

						int i = afmDto1.getSArtikelklasse().compareTo(
								afmDto2.getSArtikelklasse());

						if (i == 0) {

							if (afmDto1 != null
									&& afmDto1.getSMaterial() != null
									&& afmDto2 != null
									&& afmDto2.getSMaterial() != null) {
								i = afmDto1.getSMaterial().compareTo(
										afmDto2.getSMaterial());

								if (i == 0) {
									if (afmDto1 != null
											&& afmDto1.getDHoehe() != null
											&& afmDto2 != null
											&& afmDto2.getDHoehe() != null) {
										i = afmDto1.getDHoehe().compareTo(
												afmDto2.getDHoehe());
										if (i == 0) {
											if (afmDto1 != null
													&& afmDto1.getSIdent() != null
													&& afmDto2 != null
													&& afmDto2.getSIdent() != null) {
												i = afmDto1
														.getSIdent()
														.compareTo(
																afmDto2.getSIdent());
											}
										}
									}
								}
							}
						}
						return i;
					} else {
						return 0;
					}
				} else {
					return j;
				}

			} else {
				if (afmDto1 != null && afmDto1.getSArtikelklasse() != null
						&& afmDto2 != null
						&& afmDto2.getSArtikelklasse() != null) {

					int i = afmDto1.getSArtikelklasse().compareTo(
							afmDto2.getSArtikelklasse());

					if (i == 0) {

						if (afmDto1 != null && afmDto1.getSMaterial() != null
								&& afmDto2 != null
								&& afmDto2.getSMaterial() != null) {
							i = afmDto1.getSMaterial().compareTo(
									afmDto2.getSMaterial());

							if (i == 0) {
								if (afmDto1 != null
										&& afmDto1.getDHoehe() != null
										&& afmDto2 != null
										&& afmDto2.getDHoehe() != null) {
									i = afmDto1.getDHoehe().compareTo(
											afmDto2.getDHoehe());
									if (i == 0) {
										if (afmDto1 != null
												&& afmDto1.getSIdent() != null
												&& afmDto2 != null
												&& afmDto2.getSIdent() != null) {
											i = afmDto1.getSIdent().compareTo(
													afmDto2.getSIdent());
										}
									}
								}
							}
						}
					}
					return i;
				} else {
					return 0;
				}
			}

		}
		default: {
			return 0;
		}
		}
	}
}
