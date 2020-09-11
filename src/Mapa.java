import java.util.Random;

public class Mapa {
	public int tamsala = 4;
	Random gerador = new Random();
	public boolean flecha = true;
	public boolean wumppus = true;
	public int wumpusL;
	public int wumpusC;
	
	public void definicaoSalas(Sala[][] sala) {
		for (int i = 0; i < tamsala; i++) {
			for (int j = 0; j < tamsala; j++) {
				sala[i][j] = new Sala();
			}
		}
	}
	
	public void criaOuro(Sala[][] sala) {
		boolean control = false;
		int i = 0, j = 0;
		//não permite q o ouro esteja onde há poço
		while (control == false) {
			i = gerador.nextInt(4);
			j = gerador.nextInt(4);
			if (sala[i][j].poco == true || (i == 0 && j == 0))
				control = false;
			else {
				control = true;
				sala[i][j].ouro = true;
			}
		}
	}
	
	public void criaWumppus(Sala[][] sala) {
		boolean control = false;
		while (control == false) {
			int i = gerador.nextInt(4);
			int j = gerador.nextInt(4);
			//não permite q o wumppus esteja nos adjacentes do inicial
			if ((i == 0 & j == 0) || (i == 0 && j == 1) || (i == 1 && j == 0)) {
				control = false;
			} else {
				control = true;
				sala[i][j].wumppus = true;
				wumpusL = i;
				wumpusC = j;
				//adiciona o fedor
				if(i - 1 >= 0)
					sala[i-1][j].fedor = true;
				if(i + 1 < tamsala)
					sala[i+1][j].fedor = true;
				if(j - 1 >= 0)
					sala[i][j-1].fedor = true;
				if(j + 1 < tamsala)
					sala[i][j+1].fedor = true;
			}
		}
	}
	
	public void criaPocos(Sala[][] sala) {
		int numMax = 3;
		int k = 0;
		int num = 0;
		for (int i = 0; i < tamsala && num < numMax; i++) {
			for (int j = 0; j < tamsala && num < numMax; j++) {
				if(i != 0 && j != 0) {
					k = gerador.nextInt(10);
					if(k < 3) {
						sala[i][j].poco = true;
						num++;
						//adiciona as brisas
						if(i - 1 >= 0)
							sala[i-1][j].brisa = true;
						if(i + 1 < tamsala)
							sala[i+1][j].brisa = true;
						if(j - 1 >= 0)
							sala[i][j-1].brisa = true;
						if(j + 1 < tamsala)
							sala[i][j+1].brisa = true;		
					}	
				}
			}
		}
	}
	
	public void imprimeJogo(Sala[][] sala) {
		int cont = 0;
		for (int i = 0; i < tamsala; i++) {
			for (int j = 0; j < tamsala; j++) {
				cont = 0;
				if(sala[i][j].visitado == true) {
					System.out.print("x");
					cont++;
				}
				if(sala[i][j].wumppus == true) {
					System.out.print("W");
					cont++;
				}
				if(sala[i][j].poco == true) {
					System.out.print("P");
					cont++;
				}
				if(sala[i][j].brisa == true) {
					System.out.print("B");
					cont++;
				}
				if(sala[i][j].fedor == true) {
					System.out.print("F");
					cont++;
				}
				if(sala[i][j].ouro == true) {
					System.out.print("R");
					cont++;
				}
				if(sala[i][j].jogador == true) {
					System.out.print("G");
					cont++;
				}	
				int dif = 5 - cont;
				for(int k = 0; k < dif; k++)
					System.out.print(" ");
				if (cont == 0)
					System.out.print("");
				System.out.print(" |");
			}
			System.out.println();
		}
		System.out.println();
	}
}
	
