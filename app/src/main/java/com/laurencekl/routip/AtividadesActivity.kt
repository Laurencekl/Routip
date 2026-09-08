package com.laurencekl.routip

import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ListView
import android.widget.SimpleAdapter
import android.widget.TextView
import android.widget.Toast
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
        val preferenciasTexto = intent.getStringExtra(ViagemExtras.PREFERENCIAS).orEmpty()
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
        configurarLista(atividades)
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

    private fun configurarLista(atividades: List<Atividade>) {
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
            Log.d("Routip", "Tela 2: atividade escolhida=${atividade.titulo}")
            Toast.makeText(
                this,
                getString(R.string.atividade_selecionada, atividade.titulo),
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
