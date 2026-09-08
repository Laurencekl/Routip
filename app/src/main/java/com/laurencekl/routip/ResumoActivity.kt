package com.laurencekl.routip

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
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
        val titulo = intent.getStringExtra(ViagemExtras.ATIVIDADE_TITULO).orEmpty()
        val descricao = intent.getStringExtra(ViagemExtras.ATIVIDADE_DESCRICAO).orEmpty()
        val categoria = intent.getStringExtra(ViagemExtras.ATIVIDADE_CATEGORIA).orEmpty()
        val icone = intent.getIntExtra(ViagemExtras.ATIVIDADE_ICONE, R.drawable.ic_location)
        val duracao = intent.getIntExtra(ViagemExtras.DURACAO, 1)
        val dificuldade = intent.getStringExtra(ViagemExtras.DIFICULDADE).orEmpty()

        findViewById<TextView>(R.id.textoDestinoResumo).text = destino
        findViewById<TextView>(R.id.textoPeriodoResumo).text = getString(
            R.string.periodo_resumo,
            dataIda,
            dataVolta
        )
        findViewById<TextView>(R.id.textoPreferenciasResumo).text =
            preferencias.replace(",", ", ")
        findViewById<TextView>(R.id.textoAtividadeResumo).text = titulo
        findViewById<TextView>(R.id.textoCategoriaResumo).text = categoria
        findViewById<TextView>(R.id.textoDescricaoResumo).text = descricao
        findViewById<TextView>(R.id.textoDuracaoResumo).text = resources.getQuantityString(
            R.plurals.quantidade_horas,
            duracao,
            duracao
        )
        findViewById<TextView>(R.id.textoDificuldadeResumo).text = dificuldade
        findViewById<ImageView>(R.id.imagemResumo).setImageResource(icone)

        findViewById<ImageButton>(R.id.botaoVoltarResumo).setOnClickListener {
            finish()
        }

        findViewById<Button>(R.id.botaoFinalizar).setOnClickListener {
            val dados = "Destino=$destino, ida=$dataIda, volta=$dataVolta, " +
                "preferências=$preferencias, atividade=$titulo, " +
                "duração=$duracao, dificuldade=$dificuldade"
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
}
