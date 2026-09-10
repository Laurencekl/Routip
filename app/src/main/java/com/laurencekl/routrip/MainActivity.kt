package com.laurencekl.routrip

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var spinnerDestino: Spinner
    private lateinit var botaoIda: Button
    private lateinit var botaoVolta: Button
    private lateinit var checkGastronomia: CheckBox
    private lateinit var checkNatureza: CheckBox
    private lateinit var checkFotografia: CheckBox

    private var dataIda: Calendar? = null
    private var dataVolta: Calendar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        spinnerDestino = findViewById(R.id.spinnerDestino)
        botaoIda = findViewById(R.id.botaoDataIda)
        botaoVolta = findViewById(R.id.botaoDataVolta)
        checkGastronomia = findViewById(R.id.checkGastronomia)
        checkNatureza = findViewById(R.id.checkNatureza)
        checkFotografia = findViewById(R.id.checkFotografia)

        configurarDestinos()

        botaoIda.setOnClickListener {
            abrirCalendario(dataMinima = Calendar.getInstance()) { dataEscolhida ->
                dataIda = dataEscolhida
                botaoIda.text = formatarData(dataEscolhida)

                if (dataVolta?.before(dataEscolhida) == true) {
                    dataVolta = null
                    botaoVolta.setText(R.string.selecionar_data)
                    mensagem(getString(R.string.escolha_nova_volta))
                }
            }
        }

        botaoVolta.setOnClickListener {
            val ida = dataIda
            if (ida == null) {
                mensagem(getString(R.string.selecione_ida_primeiro))
            } else {
                abrirCalendario(dataMinima = ida) { dataEscolhida ->
                    dataVolta = dataEscolhida
                    botaoVolta.text = formatarData(dataEscolhida)
                }
            }
        }

        findViewById<Button>(R.id.botaoContinuar).setOnClickListener {
            validarFormulario()
        }
    }

    private fun configurarDestinos() {
        ArrayAdapter.createFromResource(
            this,
            R.array.destinos,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerDestino.adapter = adapter
        }
    }

    private fun abrirCalendario(
        dataMinima: Calendar,
        aoSelecionar: (Calendar) -> Unit
    ) {
        val dataInicial = dataMinima.clone() as Calendar

        DatePickerDialog(
            this,
            { _, ano, mes, dia ->
                val dataEscolhida = Calendar.getInstance().apply {
                    set(ano, mes, dia, 0, 0, 0)
                    set(Calendar.MILLISECOND, 0)
                }
                aoSelecionar(dataEscolhida)
            },
            dataInicial.get(Calendar.YEAR),
            dataInicial.get(Calendar.MONTH),
            dataInicial.get(Calendar.DAY_OF_MONTH)
        ).apply {
            datePicker.minDate = inicioDoDia(dataMinima).timeInMillis
            show()
        }
    }

    private fun validarFormulario() {
        if (spinnerDestino.selectedItemPosition == 0) {
            mensagem(getString(R.string.escolha_destino))
            return
        }

        val ida = dataIda
        val volta = dataVolta
        if (ida == null || volta == null) {
            mensagem(getString(R.string.escolha_datas))
            return
        }

        if (volta.before(ida)) {
            mensagem(getString(R.string.volta_invalida))
            return
        }

        val preferencias = mutableListOf<String>()
        if (checkGastronomia.isChecked) preferencias.add(checkGastronomia.text.toString())
        if (checkNatureza.isChecked) preferencias.add(checkNatureza.text.toString())
        if (checkFotografia.isChecked) preferencias.add(checkFotografia.text.toString())

        if (preferencias.isEmpty()) {
            mensagem(getString(R.string.escolha_preferencia))
            return
        }

        val resumo = getString(
            R.string.resumo_configuracao,
            spinnerDestino.selectedItem.toString(),
            formatarData(ida),
            formatarData(volta),
            preferencias.joinToString(", ")
        )

        AlertDialog.Builder(this)
            .setTitle(R.string.tudo_certo)
            .setMessage(resumo)
            .setPositiveButton(R.string.ver_atividades) { _, _ ->
                Log.d("Routrip", "Tela 1 -> Tela 2: $resumo")

                val intent = Intent(this, AtividadesActivity::class.java)
                intent.putExtra(ViagemExtras.DESTINO, spinnerDestino.selectedItem.toString())
                intent.putExtra(ViagemExtras.DATA_IDA, formatarData(ida))
                intent.putExtra(ViagemExtras.DATA_VOLTA, formatarData(volta))
                intent.putExtra(ViagemExtras.PREFERENCIAS, preferencias.joinToString(","))
                startActivity(intent)
            }
            .setNegativeButton(R.string.cancelar, null)
            .show()
    }

    private fun formatarData(data: Calendar): String {
        return SimpleDateFormat("dd/MM/yyyy", Locale.forLanguageTag("pt-BR")).format(data.time)
    }

    private fun inicioDoDia(data: Calendar): Calendar {
        return (data.clone() as Calendar).apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
    }

    private fun mensagem(texto: String) {
        Toast.makeText(this, texto, Toast.LENGTH_SHORT).show()
    }
}
