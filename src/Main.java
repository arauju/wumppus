
public class Main {

	public static void main(String[] args) {
		Mapa mapa = new Mapa();
		Sala[][] mapaJogador = new Sala[mapa.tamsala][mapa.tamsala] ;
		Sala[][] mapaJogo = new Sala[mapa.tamsala][mapa.tamsala];
		Agente agente = new Agente();
		mapa.definicaoSalas(mapaJogo);
		mapa.definicaoSalas(mapaJogador);
		boolean fimDeJogo = false;
		
		//cria o wumppus
		mapa.criaWumppus(mapaJogo);
		
		//cria os pocos
		mapa.criaPocos(mapaJogo);
		
		//cria ouro
		mapa.criaOuro(mapaJogo);
		
		//imprime mapa do jogo
		System.out.println("Mapa jogo:");
		mapa.imprimeJogo(mapaJogo);
		System.out.println();
		
		//Inicia jogo
		fimDeJogo = agente.atualizaSala(0, 0, mapaJogo, mapaJogador, true);
		while (fimDeJogo == false) {
			//guarda a posição
			int guardaI = agente.getSalaAtualLinha();
			int guardaJ = agente.getSalaAtualColuna();
			
			//faz movimento
			agente.analisaSentidos(mapaJogador, mapaJogo);
			fimDeJogo = agente.escolheMovimento(mapaJogador, mapaJogo);
			
			//se nenhum movimento foi feito, faz um aleatorio
			if (guardaI == agente.getSalaAtualLinha() && guardaJ == agente.getSalaAtualColuna())
				agente.movimentoQualquer(mapaJogador, mapaJogo);
		}
		System.out.println("");
		System.out.println("Mapa do guerreiro final:");
		mapa.imprimeJogo(mapaJogador);
		System.out.print("Medida de desempenho final: "+agente.medidaDesempenho);
	}
}
