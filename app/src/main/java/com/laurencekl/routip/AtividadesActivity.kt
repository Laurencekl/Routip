package com.laurencekl.routip

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ListView
import android.widget.SimpleAdapter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class AtividadesActivity : AppCompatActivity() {

    data class Atividade(
        val titulo: String,
        val descricao: String,
        val categoria: String,
        val icone: Int
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_atividades)

        val destino = intent.getStringExtra(ViagemExtras.DESTINO).orEmpty()
        val dataIda = intent.getStringExtra(ViagemExtras.DATA_IDA).orEmpty()
        val dataVolta = intent.getStringExtra(ViagemExtras.DATA_VOLTA).orEmpty()
        val preferenciasTexto = intent.getStringExtra(ViagemExtras.PREFERENCIAS).orEmpty()
        val titulosAdicionados = intent.getStringArrayListExtra(
            ViagemExtras.ATIVIDADES_TITULOS
        ) ?: arrayListOf()
        val descricoesAdicionadas = intent.getStringArrayListExtra(
            ViagemExtras.ATIVIDADES_DESCRICOES
        ) ?: arrayListOf()
        val categoriasAdicionadas = intent.getStringArrayListExtra(
            ViagemExtras.ATIVIDADES_CATEGORIAS
        ) ?: arrayListOf()
        val iconesAdicionados = intent.getIntegerArrayListExtra(
            ViagemExtras.ATIVIDADES_ICONES
        ) ?: arrayListOf()
        val duracoesAdicionadas = intent.getIntegerArrayListExtra(
            ViagemExtras.ATIVIDADES_DURACOES
        ) ?: arrayListOf()
        val dificuldadesAdicionadas = intent.getStringArrayListExtra(
            ViagemExtras.ATIVIDADES_DIFICULDADES
        ) ?: arrayListOf()
        val preferencias = preferenciasTexto.split(",").filter { it.isNotBlank() }

        findViewById<TextView>(R.id.textoDestino).text = destino
        findViewById<TextView>(R.id.textoFiltros).text = getString(
            R.string.filtros_selecionados,
            preferencias.joinToString(", ")
        )

        findViewById<ImageButton>(R.id.botaoVoltar).setOnClickListener {
            finish()
        }

        val atividades = filtrarAtividades(preferencias)
        configurarLista(
            atividades,
            destino,
            dataIda,
            dataVolta,
            preferenciasTexto,
            titulosAdicionados,
            descricoesAdicionadas,
            categoriasAdicionadas,
            iconesAdicionados,
            duracoesAdicionadas,
            dificuldadesAdicionadas
        )
    }

    private fun filtrarAtividades(preferencias: List<String>): List<Atividade> {
        val todasAtividades = listOf(
            Atividade(
                "Tour gastronômico",
                "Experimente pratos e sabores típicos da região.",
                "Gastronomia",
                R.drawable.ic_food
            ),
            Atividade(
                "Aula de culinária",
                "Aprenda uma receita local com um cozinheiro da cidade.",
                "Gastronomia",
                R.drawable.ic_food
            ),
            Atividade(
                "Trilha no parque",
                "Caminhe por uma área verde e observe a paisagem.",
                "Natureza",
                R.drawable.ic_nature
            ),
            Atividade(
                "Passeio ecológico",
                "Conheça a fauna e a flora com um guia local.",
                "Natureza",
                R.drawable.ic_nature
            ),
            Atividade(
                "Tour fotográfico",
                "Visite pontos especiais para registrar boas imagens.",
                "Fotografia",
                R.drawable.ic_camera
            ),
            Atividade(
                "Pôr do sol no mirante",
                "Fotografe a cidade durante o fim da tarde.",
                "Fotografia",
                R.drawable.ic_camera
            )
        )

        return todasAtividades.filter { atividade ->
            preferencias.contains(atividade.categoria)
        }
    }

    private fun configurarLista(
        atividades: List<Atividade>,
        destino: String,
        dataIda: String,
        dataVolta: String,
        preferencias: String,
        titulosAdicionados: ArrayList<String>,
        descricoesAdicionadas: ArrayList<String>,
        categoriasAdicionadas: ArrayList<String>,
        iconesAdicionados: ArrayList<Int>,
        duracoesAdicionadas: ArrayList<Int>,
        dificuldadesAdicionadas: ArrayList<String>
    ) {
        val dadosLista = atividades.map { atividade ->
            hashMapOf<String, Any>(
                "icone" to atividade.icone,
                "titulo" to atividade.titulo,
                "descricao" to atividade.descricao,
                "categoria" to atividade.categoria
            )
        }

        val adapter = SimpleAdapter(
            this,
            dadosLista,
            R.layout.item_atividade,
            arrayOf("icone", "titulo", "descricao", "categoria"),
            intArrayOf(
                R.id.imagemAtividade,
                R.id.tituloAtividade,
                R.id.descricaoAtividade,
                R.id.categoriaAtividade
            )
        )

        val lista = findViewById<ListView>(R.id.listaAtividades)
        lista.adapter = adapter
        lista.setOnItemClickListener { _, _, posicao, _ ->
            val atividade = atividades[posicao]
            Log.d("Routip", "Tela 2 -> Tela 3: atividade=${atividade.titulo}")

            val intent = Intent(this, DetalhesActivity::class.java)
            intent.putExtra(ViagemExtras.DESTINO, destino)
            intent.putExtra(ViagemExtras.DATA_IDA, dataIda)
            intent.putExtra(ViagemExtras.DATA_VOLTA, dataVolta)
            intent.putExtra(ViagemExtras.PREFERENCIAS, preferencias)
            intent.putExtra(ViagemExtras.ATIVIDADE_TITULO, atividade.titulo)
            intent.putExtra(ViagemExtras.ATIVIDADE_DESCRICAO, atividade.descricao)
            intent.putExtra(ViagemExtras.ATIVIDADE_CATEGORIA, atividade.categoria)
            intent.putExtra(ViagemExtras.ATIVIDADE_ICONE, atividade.icone)
            intent.putStringArrayListExtra(ViagemExtras.ATIVIDADES_TITULOS, titulosAdicionados)
            intent.putStringArrayListExtra(ViagemExtras.ATIVIDADES_DESCRICOES, descricoesAdicionadas)
            intent.putStringArrayListExtra(ViagemExtras.ATIVIDADES_CATEGORIAS, categoriasAdicionadas)
            intent.putIntegerArrayListExtra(ViagemExtras.ATIVIDADES_ICONES, iconesAdicionados)
            intent.putIntegerArrayListExtra(ViagemExtras.ATIVIDADES_DURACOES, duracoesAdicionadas)
            intent.putStringArrayListExtra(
                ViagemExtras.ATIVIDADES_DIFICULDADES,
                dificuldadesAdicionadas
            )
            startActivity(intent)
        }
    }
}
