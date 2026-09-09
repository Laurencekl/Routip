package com.laurencekl.routip

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class ResumoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resumo)

        val destino = intent.getStringExtra(ViagemExtras.DESTINO).orEmpty()
        val dataIda = intent.getStringExtra(ViagemExtras.DATA_IDA).orEmpty()
        val dataVolta = intent.getStringExtra(ViagemExtras.DATA_VOLTA).orEmpty()
        val preferencias = intent.getStringExtra(ViagemExtras.PREFERENCIAS).orEmpty()
        val titulos = intent.getStringArrayListExtra(
            ViagemExtras.ATIVIDADES_TITULOS
        ) ?: arrayListOf()
        val descricoes = intent.getStringArrayListExtra(
            ViagemExtras.ATIVIDADES_DESCRICOES
        ) ?: arrayListOf()
        val categorias = intent.getStringArrayListExtra(
            ViagemExtras.ATIVIDADES_CATEGORIAS
        ) ?: arrayListOf()
        val icones = intent.getIntegerArrayListExtra(
            ViagemExtras.ATIVIDADES_ICONES
        ) ?: arrayListOf()
        val duracoes = intent.getIntegerArrayListExtra(
            ViagemExtras.ATIVIDADES_DURACOES
        ) ?: arrayListOf()
        val dificuldades = intent.getStringArrayListExtra(
            ViagemExtras.ATIVIDADES_DIFICULDADES
        ) ?: arrayListOf()

        findViewById<TextView>(R.id.textoDestinoResumo).text = destino
        findViewById<TextView>(R.id.textoPeriodoResumo).text = getString(
            R.string.periodo_resumo,
            dataIda,
            dataVolta
        )
        findViewById<TextView>(R.id.textoPreferenciasResumo).text =
            preferencias.replace(",", ", ")

        mostrarAtividades(titulos, descricoes, categorias, icones, duracoes, dificuldades)

        findViewById<ImageButton>(R.id.botaoVoltarResumo).setOnClickListener {
            finish()
        }

        findViewById<Button>(R.id.botaoAdicionarOutra).setOnClickListener {
            Log.d("Routip", "Tela 4 -> Tela 2: adicionar outra atividade")

            val intent = Intent(this, AtividadesActivity::class.java)
            intent.putExtra(ViagemExtras.DESTINO, destino)
            intent.putExtra(ViagemExtras.DATA_IDA, dataIda)
            intent.putExtra(ViagemExtras.DATA_VOLTA, dataVolta)
            intent.putExtra(ViagemExtras.PREFERENCIAS, preferencias)
            intent.putStringArrayListExtra(ViagemExtras.ATIVIDADES_TITULOS, titulos)
            intent.putStringArrayListExtra(ViagemExtras.ATIVIDADES_DESCRICOES, descricoes)
            intent.putStringArrayListExtra(ViagemExtras.ATIVIDADES_CATEGORIAS, categorias)
            intent.putIntegerArrayListExtra(ViagemExtras.ATIVIDADES_ICONES, icones)
            intent.putIntegerArrayListExtra(ViagemExtras.ATIVIDADES_DURACOES, duracoes)
            intent.putStringArrayListExtra(ViagemExtras.ATIVIDADES_DIFICULDADES, dificuldades)
            startActivity(intent)
        }

        findViewById<Button>(R.id.botaoFinalizar).setOnClickListener {
            val dados = "Destino=$destino, ida=$dataIda, volta=$dataVolta, " +
                "preferências=$preferencias, atividades=${titulos.joinToString()}"
            Log.d("Routip", "Tela 4 finalizada: $dados")

            AlertDialog.Builder(this)
                .setTitle(R.string.viagem_planejada)
                .setMessage(R.string.mensagem_finalizacao)
                .setPositiveButton(R.string.voltar_inicio) { _, _ ->
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }
                .show()
        }
    }

    private fun mostrarAtividades(
        titulos: ArrayList<String>,
        descricoes: ArrayList<String>,
        categorias: ArrayList<String>,
        icones: ArrayList<Int>,
        duracoes: ArrayList<Int>,
        dificuldades: ArrayList<String>
    ) {
        val lista = findViewById<LinearLayout>(R.id.listaAtividadesResumo)

        for (indice in titulos.indices) {
            val item = layoutInflater.inflate(
                R.layout.item_resumo_atividade,
                lista,
                false
            )

            item.findViewById<TextView>(R.id.tituloItemResumo).text = titulos[indice]
            item.findViewById<TextView>(R.id.descricaoItemResumo).text = descricoes[indice]
            item.findViewById<TextView>(R.id.categoriaItemResumo).text = categorias[indice]
            item.findViewById<ImageView>(R.id.imagemItemResumo).setImageResource(icones[indice])
            item.findViewById<TextView>(R.id.duracaoItemResumo).text =
                resources.getQuantityString(
                    R.plurals.quantidade_horas,
                    duracoes[indice],
                    duracoes[indice]
                )
            item.findViewById<TextView>(R.id.dificuldadeItemResumo).text = dificuldades[indice]

            lista.addView(item)
        }
    }
}
