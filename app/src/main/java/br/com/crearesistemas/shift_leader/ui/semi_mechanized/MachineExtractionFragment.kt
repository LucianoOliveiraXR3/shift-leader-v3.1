package br.com.crearesistemas.shift_leader.ui.semi_mechanized

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.fragment.app.Fragment
import br.com.crearesistemas.shift_leader.R
import br.com.crearesistemas.shift_leader.db_service.model.AppointmentSemiMechanized
import br.com.crearesistemas.shift_leader.db_service.viewmodel.AppointmentSemiMechanizedViewModel
import br.com.crearesistemas.shift_leader.db_service.viewmodel.MachineGroupViewModel
import br.com.crearesistemas.shift_leader.dto.MachineExtractionDto
import br.com.crearesistemas.shift_leader.ui.util.DialogUtil

class MachineExtractionFragment : Fragment() {

    val LOG_TAG = "Machine_Extraction"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_machine_extraction, container, false)

        val buttonSave = root.findViewById<Button>(R.id.btn_save)

        val txtSizeVol = root.findViewById<EditText>(R.id.txt_size_vol)
        val spinner = root.findViewById<Spinner>(R.id.select_gang)

        val gangService = MachineGroupViewModel(requireActivity().application)
        val groups = gangService.getAll()

        val spinnerArrayAdapter = ArrayAdapter(
            requireContext(),
            R.layout.spinner_item,
            groups.map { it.descriptionPrimary }
        )
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = spinnerArrayAdapter

        val userId = 1L

        buttonSave.setOnClickListener {
            DialogUtil().openSuccessDialog(requireContext())
            try {

                val machineExtraction = MachineExtractionDto()
                machineExtraction.sizeVolIsAPreset = txtSizeVol.text.toString().toFloat()
                machineExtraction.gang = groups[spinner.selectedItemPosition].descriptionPrimary

                val appoint = AppointmentSemiMechanized().apply {

                    this.origin = "Semi_Mechanized"
                    this.type = "Machine_Extraction"
                    this.data = machineExtraction.toJson()
                    this.userId = userId
                }

                val appointSemiMechanizedService =
                    AppointmentSemiMechanizedViewModel(requireActivity().application)

                appointSemiMechanizedService.save(appoint)

                DialogUtil().openSuccessDialog(requireContext())

            } catch (e: Exception) {
                e.printStackTrace()
                e.localizedMessage?.let { it1 -> Log.d(LOG_TAG, it1) }
                DialogUtil().openErrorDialog(requireContext())
            }
        }

        return root
    }

}