
public class Agente {
	Mapa mapa = new Mapa();
	private int novaLinha;
	private int novaColuna;
	private int adj = 0;
	private float mediaAtual = 99999.9f;
	public int medidaDesempenho = 0;
	public boolean wumppusConhecido = false;
	public int wumppusLinha;
	public int wumppusColuna;
	public int salaAtualLinha;
	public int salaAtualColuna;
	
	public int getSalaAtualLinha() {
		return salaAtualLinha;
	}
	
	public int getSalaAtualColuna() {
		return salaAtualColuna;
	}
	
	boolean atualizaSala(int linha, int coluna, Sala[][] mapaJogo, Sala[][] mapaJogador, boolean bool) {
		mapaJogo[linha][coluna].jogador = bool;
		mapaJogador[linha][coluna].jogador = bool;
		salaAtualLinha = linha;
		salaAtualColuna = coluna;
		mapaJogador[linha][coluna].visitado = true;

		if (bool == true) { /*está entrando em uma nova sala*/
			if (mapaJogo[linha][coluna].wumppus == true) {
				mapaJogador[linha][coluna].poco = mapaJogo[linha][coluna].poco;
				mapaJogador[linha][coluna].wumppus = mapaJogo[linha][coluna].wumppus;
				System.out.println("O guerreiro entrou em uma sala com o Wumppus e morreu!");
				medidaDesempenho -= 1000;
				mapa.imprimeJogo(mapaJogador);
				return true;
			} else {
				if (mapaJogo[linha][coluna].poco == true) {
					mapaJogador[linha][coluna].poco = mapaJogo[linha][coluna].poco;
					System.out.println("O guerreiro entrou em uma sala com um poço e morreu!");
					medidaDesempenho -= 1000;
					mapa.imprimeJogo(mapaJogador);
					return true;
				} else {
					if (mapaJogo[linha][coluna].ouro == true) {
						mapaJogador[linha][coluna].ouro = mapaJogo[linha][coluna].ouro;
						System.out.println("O guerreiro encontrou o ouro, fim de jogo!");
						medidaDesempenho += 1000;
						mapa.imprimeJogo(mapaJogador);
						return true;	
					} else {
						//capta os sentidos na sala
						mapaJogador[linha][coluna].brisa = mapaJogo[linha][coluna].brisa;
						mapaJogador[linha][coluna].fedor = mapaJogo[linha][coluna].fedor;
						analisaSentidos(mapaJogador, mapaJogo);
						mapa.imprimeJogo(mapaJogador);
						//System.out.println();
						return false;
					}
				}
			}	
		}
		return false;
	}
	
	/*verifica se é possível inferir que existe um poço ou wumppus
	 * analisando se para cada posição, todos seus adjacentes tem uma brisa ou fedor*/
	public void analisaSentidos(Sala[][] mapaJogador, Sala[][] mapaJogo) {
		int adj = 0, contF = 0;
		int novaLinhaF = 0, novaColunaF = 0;
		for (int i = 0; i < mapa.tamsala; i++) {
			for (int j = 0; j < mapa.tamsala; j++) {
				/*analisa o fedor se o wumppus estiver vivo*/
				if(mapa.wumppus == true && wumppusConhecido == false) {
					adj = 0; contF = 0;
					if (mapaJogador[i][j].fedor == true) {
						int guardaI = i;
						int guardaJ = j;
						for(int k = 0; k < 4; k ++) {
							i = guardaI;
							j = guardaJ;
							if (k == 0)
								i = i - 1;
							if (k == 1)
								i = i + 1;
							if (k == 2)
								j = j - 1;
							if (k == 3)
								j = j + 1;
							if(i >= 0 && i < mapa.tamsala && j >= 0 && j < mapa.tamsala) {
								adj++;
								if (mapaJogador[i][j].visitado == true)
									contF++;
								else {
									novaLinhaF = i;
									novaColunaF = j;
								}
							}
						}
						i = guardaI;
						j = guardaJ;
						if (adj == contF + 1 && mapa.wumppus == true) {
							mapaJogador[novaLinhaF][novaColunaF].wumppus = true;
							mapaJogador[novaLinhaF][novaColunaF].proibido = true;
							wumppusLinha = novaLinhaF;
							wumppusColuna = novaColunaF;
							wumppusConhecido = true;
							mapa.imprimeJogo(mapaJogador);
						}
					}
				}
				
				/*analisa as brisas*/
				contF = 0; adj = 0;
				if (mapaJogador[i][j].brisa == true) {
					int guardaI = i;
					int guardaJ = j;
					for(int k = 0; k < 4; k ++) {
						i = guardaI;
						j = guardaJ;
						if (k == 0)
							i = i - 1;
						if (k == 1)
							i = i + 1;
						if (k == 2)
							j = j - 1;
						if (k == 3)
							j = j + 1;
						if(i >= 0 && i < mapa.tamsala && j >= 0 && j < mapa.tamsala) {
							adj++;
							if (mapaJogador[i][j].visitado == true)
								contF++;
							else {
								novaLinhaF = i;
								novaColunaF = j;
							}
						}
					}
					i = guardaI;
					j = guardaJ;
					if (adj == contF + 1) {
						mapaJogador[novaLinhaF][novaColunaF].poco = true;
						mapaJogador[novaLinhaF][novaColunaF].proibido = true;
					}		
				}	
			}
		}
	}
	
	public void mataWumppus(Sala[][] mapaJogador, Sala[][] mapaJogo) {
		mapa.flecha = false;
		mapa.wumppus = false;
		mapaJogador[wumppusLinha][wumppusColuna].wumppus = false;
		mapaJogo[wumppusLinha][wumppusColuna].wumppus = false;
		medidaDesempenho -= 10;
		System.out.println("O guerreiro atirou sua flecha e matou o Wumppus!");
		for (int i = 0; i < mapa.tamsala; i++) {
			for (int j = 0; j < mapa.tamsala; j++) {
				mapaJogador[i][j].fedor = false;
			}
		}
		//mapa.imprimeJogo(mapaJogador);
	}
	
	public boolean atiraFlecha(Sala[][] mapaJogador, Sala[][] mapaJogo) {
		if (mapa.wumppus == true) {
			//jogador e wummpus estão na mesma linha
			if (wumppusLinha == salaAtualLinha) {
				mataWumppus(mapaJogador, mapaJogo);
				return true;
			}
			//jogador e wumppus estão na mesma coluna
			else if (wumppusColuna == salaAtualColuna) {
				mataWumppus(mapaJogador, mapaJogo);
				return true;
			} else {
				//se move para uma linha ou coluna para usar a flecha
				for (int i = 0; i < mapa.tamsala; i++) {
					if(i == wumppusLinha && mapaJogador[i][salaAtualColuna].visitado == true) {
						medidaDesempenho -= Math.abs(salaAtualLinha - i);
						atualizaSala(salaAtualLinha, salaAtualColuna, mapaJogo, mapaJogador, false);
						atualizaSala(i, salaAtualColuna, mapaJogo, mapaJogador, true);
						mataWumppus(mapaJogador, mapaJogo);
						return true;
					}
				}
				for (int j = 0; j < mapa.tamsala; j++) {
					if(j == wumppusColuna && mapaJogador[salaAtualLinha][j].visitado == true) {
						medidaDesempenho -= Math.abs(salaAtualColuna - j);
						atualizaSala(salaAtualLinha, salaAtualColuna, mapaJogo, mapaJogador, false);
						atualizaSala(salaAtualLinha, j, mapaJogo, mapaJogador, true);
						mataWumppus(mapaJogador, mapaJogo);
						return true;
					}
				}
			}			
		}
		return false;
	}	
	
	public boolean verificaAdj (int novaLinha, int novaColuna, Sala[][] mapaJogador, Sala[][] mapaJogo) {
		if (novaLinha >= 0 && novaLinha < mapa.tamsala && novaColuna >= 0 && novaColuna < mapa.tamsala) {
			if (mapaJogador[novaLinha][novaColuna].visitado == false && 
					mapaJogador[novaLinha][novaColuna].brisa == false && 
					mapaJogador[novaLinha][novaColuna].proibido == false) {
				if (mapaJogador[novaLinha][novaColuna].fedor == false || mapa.wumppus == false) {
					this.novaLinha = novaLinha;
					this.novaColuna = novaColuna;
					return true;
				}
			}
		}
		return false;
	}
	
	
	public boolean escolheMovimento(Sala[][] mapaJogador, Sala[][] mapaJogo) {
		int linha = salaAtualLinha;
		int coluna = salaAtualColuna;
		boolean movValido = false, fimDeJogo = false;
		
		/*MOVIMENTO SEGURO
		 * se sua posição não tem brisa ou fedor, escolhe um adjacente não visitado*/
		if (mapaJogador[linha][coluna].brisa == false &&
			((mapa.wumppus == true && mapaJogador[linha][coluna].fedor == false) ||
					(mapa.wumppus == false && mapaJogador[linha][coluna].fedor == true))) {
			//procura um adjacente valido
			movValido = verificaAdj(linha + 1, coluna, mapaJogador, mapaJogo);
			if (movValido == false)
				movValido = verificaAdj(linha - 1, coluna, mapaJogador, mapaJogo);
			if (movValido == false)
				movValido = verificaAdj(linha, coluna - 1, mapaJogador, mapaJogo);
			if (movValido == false)
				movValido = verificaAdj(linha, coluna + 1, mapaJogador, mapaJogo);
			//se foi encontrado um adjacente valido, sai da sala atual e entra na nova
			if (movValido == true) {
				fimDeJogo = atualizaSala(linha, coluna, mapaJogo, mapaJogador, false);
				fimDeJogo = atualizaSala(this.novaLinha, this.novaColuna, mapaJogo, mapaJogador, true);
				medidaDesempenho -= Math.abs(this.novaLinha - linha);
				medidaDesempenho -= Math.abs(this.novaColuna - coluna);
				if (fimDeJogo == false) 
					return false; /*não é fim de jogo*/
				else 
					return true; /*avisa que é fim de jogo*/
			}
		} else {		
			//se sua posição atual tem brisa ou fedor, procura por outros movimentos seguros
			movValido = false;
			for (int i = 0; i < mapa.tamsala; i++) {
				for (int j = 0; j < mapa.tamsala; j++) {
					if (mapaJogador[i][j].visitado && mapaJogador[i][j].brisa == false && mapaJogador[i][j].fedor == false) {
						movValido = verificaAdj(i + 1, j, mapaJogador, mapaJogo);
						if (movValido == false)
							movValido = verificaAdj(i - 1, j, mapaJogador, mapaJogo);
						if (movValido == false)
							movValido = verificaAdj(i, j - 1, mapaJogador, mapaJogo);
						if (movValido == false)
							movValido = verificaAdj(i, j + 1, mapaJogador, mapaJogo);
					}
					if (movValido == true) {
						fimDeJogo = atualizaSala(linha, coluna, mapaJogo, mapaJogador, false);
						fimDeJogo = atualizaSala(this.novaLinha, this.novaColuna, mapaJogo, mapaJogador, true);
						medidaDesempenho -= Math.abs(this.novaLinha - linha);
						medidaDesempenho -= Math.abs(this.novaColuna - coluna);
						if (fimDeJogo == false)
							return false; /*não é fim de jogo*/
						else 
							return true; /*avisa que é fim de jogo*/
					}
				}
			}
			if (wumppusConhecido == true && mapaJogador[wumppusLinha][wumppusColuna].poco == false){
				//atira a flecha
				boolean atira = atiraFlecha(mapaJogador, mapaJogo);
				if (atira == true) {
					if(mapaJogador[wumppusLinha][wumppusColuna].poco == false)
						mapaJogador[wumppusLinha][wumppusColuna].proibido = false;
					analisaSentidos(mapaJogador, mapaJogo);
				}	
			}
		}	
		
		/*MOVIMENTO NÃO SEGURO*/
		//escolhe uma sala que possui o menor numero de brisas ou fedor ao redor
		this.mediaAtual = 99999999.9f; 
		for (int i = 0; i < mapa.tamsala; i++) {
			for (int j = 0; j < mapa.tamsala; j++) {
				if (mapaJogador[i][j].visitado == true && (mapaJogador[i][j].fedor == false ||  mapaJogador[i][j].brisa == false)){
					/*para cada visitado, encontra seus adjacentes não visitados (possivel movimento).
					 *  Depois, para cada possivel movimento, escolhe aquele que possui o menor numero
					 *  de brisas e fedor ao seu redor.*/
					if (i - 1 >= 0 && mapaJogador[i - 1][j].visitado == false && mapaJogador[i - 1][j].proibido == false)
						movNSeguro(i - 1, j, mapaJogador);	
					if (i + 1 < mapa.tamsala && mapaJogador[i + 1][j].visitado == false && mapaJogador[i + 1][j].proibido == false)
						movNSeguro(i + 1, j, mapaJogador);	
					if (j - 1 >= 0 && mapaJogador[i][j - 1].visitado == false && mapaJogador[i][j - 1].proibido == false)
						movNSeguro(i, j - 1, mapaJogador);
					if (j + 1 < mapa.tamsala && mapaJogador[i][j + 1].visitado == false && mapaJogador[i][j + 1].proibido == false)
						movNSeguro(i, j + 1, mapaJogador);
				} 
			}	
		}
		//faz o movimento, escolhendo a sala com menor numero de adjacentes com brisa e fedor
		medidaDesempenho -= Math.abs(salaAtualLinha - this.novaLinha);
		medidaDesempenho -= Math.abs(salaAtualColuna - this.novaColuna);
		
		fimDeJogo = atualizaSala(salaAtualLinha, salaAtualColuna, mapaJogo, mapaJogador, false);
		fimDeJogo = atualizaSala(this.novaLinha, this.novaColuna, mapaJogo, mapaJogador, true);
		if (fimDeJogo == false)
			return false; /*não é fim de jogo*/
		else 
			return true; /*avisa que é fim de jogo*/
	}
	
	public void movNSeguro (int novaLinha, int novaColuna, Sala[][] mapaJogador){
		this.adj = 0;
		float encontrado = 0;
		boolean movValido;
		movValido = testaMovNSeguro(novaLinha - 1, novaColuna, mapaJogador);
		if (movValido == false) encontrado ++;
		movValido = testaMovNSeguro(novaLinha + 1, novaColuna, mapaJogador);
		if (movValido == false) encontrado ++;
		movValido = testaMovNSeguro(novaLinha, novaColuna - 1, mapaJogador);
		if (movValido == false) encontrado ++;
		movValido = testaMovNSeguro(novaLinha, novaColuna + 1, mapaJogador);
		if (movValido == false) encontrado ++;
		float media = encontrado / this.adj;
		if (media <= this.mediaAtual) {
			this.mediaAtual = media;
			this.novaColuna = novaColuna;
			this.novaLinha = novaLinha;
		}	
	}
	
	public boolean testaMovNSeguro(int novaLinha, int novaColuna, Sala[][] mapaJogador) {
		if (novaLinha >= 0 && novaLinha < mapa.tamsala && novaColuna >= 0 && novaColuna < mapa.tamsala){
			this.adj++;
			if (mapaJogador[novaLinha][novaColuna].brisa == true)
				return false; /*existe brisa ao redor desse adj*/
			Mapa mapa = new Mapa();
			if (mapa.wumppus == true && mapaJogador[novaLinha][novaColuna].fedor == true)
				return false; /*existe fedor ao redor desse adj*/
		}		
		return true;
	}
	
	public void movimentoQualquer(Sala[][] mapaJogador, Sala[][] mapaJogo){
		//Se não foi possível fazer um movimento saindo de uma sala que não tenha brisa E fedor, escolhe qualquer movimento possivel
		if (wumppusConhecido == true && mapaJogador[wumppusLinha][wumppusColuna].poco == false){
			//atira a flecha
			boolean atira = atiraFlecha(mapaJogador, mapaJogo);
			if (atira == true) {
				if(mapaJogador[wumppusLinha][wumppusColuna].poco == false)
					mapaJogador[wumppusLinha][wumppusColuna].proibido = false;
				analisaSentidos(mapaJogador, mapaJogo);
			}	
		}
		for (int i = 0; i < mapa.tamsala; i++) {
			for (int j = 0; j < mapa.tamsala; j++) {
				if (mapaJogador[i][j].visitado == true) {
					if (j - 1 >= 0 && mapaJogador[i][j - 1].visitado == false && mapaJogador[i][j - 1].proibido == false) {
						this.novaColuna = j - 1;
						this.novaLinha = i;
					}
					if (j + 1 < mapa.tamsala && mapaJogador[i][j + 1].visitado == false && mapaJogador[i][j + 1].proibido == false) {
						this.novaColuna = j + 1;
						this.novaLinha = i;;	
					}
					if (i - 1 >= 0 && mapaJogador[i - 1][j].visitado == false && mapaJogador[i - 1][j].proibido == false) {
						this.novaColuna = j;
						this.novaLinha = i - 1;;
					}
					if (i + 1 < mapa.tamsala && mapaJogador[i + 1][j].visitado == false && mapaJogador[i + 1][j].proibido == false) {
						this.novaColuna = j;
						this.novaLinha = i + 1;;
					}
					if (mapaJogador[i][j].fedor == true && mapaJogador[this.novaLinha][this.novaColuna].poco == false) {
						wumppusLinha = this.novaLinha;
						wumppusColuna = this.novaColuna;
						atiraFlecha(mapaJogador, mapaJogo);
					}
				}	
			}
		}
		
	}
}
