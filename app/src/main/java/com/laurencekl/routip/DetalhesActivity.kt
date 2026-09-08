package com.laurencekl.routip

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class DetalhesActivity : AppCompatActivity() {

    private lateinit var textoDuracao: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalhes)

        val titulo = intent.getStringExtra(ViagemExtras.ATIVIDADE_TITULO).orEmpty()
        val descricao = intent.getStringExtra(ViagemExtras.ATIVIDADE_DESCRICAO).orEmpty()
        val categoria = intent.getStringExtra(ViagemExtras.ATIVIDADE_CATEGORIA).orEmpty()
        val icone = intent.getIntExtra(ViagemExtras.ATIVIDADE_ICONE, R.drawable.ic_location)

        findViewById<TextView>(R.id.tituloDetalhes).text = titulo
        findViewById<TextView>(R.id.descricaoDetalhes).text = descricao
        findViewById<TextView>(R.id.categoriaDetalhes).text = categoria
        findViewById<ImageView>(R.id.imagemDetalhes).setImageResource(icone)

        findViewById<ImageButton>(R.id.botaoVoltarDetalhes).setOnClickListener {
            finish()
        }

        textoDuracao = findViewById(R.id.textoDuracao)
        val seekDuracao = findViewById<SeekBar>(R.id.seekDuracao)
        atualizarDuracao(seekDuracao.progress)

        seekDuracao.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, valor: Int, usuario: Boolean) {
                atualizarDuracao(valor)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // Não é necessário executar nenhuma ação aqui.
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // Não é necessário executar nenhuma ação aqui.
            }
        })

        findViewById<Button>(R.id.botaoAdicionar).setOnClickListener {
            confirmarAtividade(titulo, seekDuracao.progress)
        }
    }

    private fun atualizarDuracao(horas: Int) {
        textoDuracao.text = resources.getQuantityString(
            R.plurals.quantidade_horas,
            horas,
            horas
        )
    }

    private fun confirmarAtividade(titulo: String, duracao: Int) {
        val radioDificuldade = findViewById<RadioGroup>(R.id.radioDificuldade)
        val dificuldade = when (radioDificuldade.checkedRadioButtonId) {
            R.id.radioFacil -> getString(R.string.facil)
            R.id.radioDificil -> getString(R.string.dificil)
            else -> getString(R.string.moderada)
        }

        val resumo = getString(
            R.string.resumo_atividade,
            titulo,
            duracao,
            dificuldade
        )

        Log.d("Routip", "Tela 3: $resumo")

        AlertDialog.Builder(this)
            .setTitle(R.string.atividade_adicionada)
            .setMessage(resumo)
            .setPositiveButton(R.string.ok, null)
            .show()
    }
}
