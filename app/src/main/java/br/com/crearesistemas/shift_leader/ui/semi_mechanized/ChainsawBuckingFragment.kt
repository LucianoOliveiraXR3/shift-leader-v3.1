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
import br.com.crearesistemas.shift_leader.dto.ChainsawBuckingDto
import br.com.crearesistemas.shift_leader.ui.util.DialogUtil

class ChainsawBuckingFragment : Fragment() {

    private val LOG_TAG = "Chainsaw_Bucking"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_chainsaw_bucking, container, false)

        val buttonSave = root.findViewById<Button>(R.id.btn_save)

        val txtLocation = root.findViewById<EditText>(R.id.txt_location)
        val txtQuantityChainsawMen = root.findViewById<EditText>(R.id.txt_quantity_chainsaw_men)
        val txtAreaSize = root.findViewById<EditText>(R.id.txt_area_size)
        val txtWorkingHours = root.findViewById<EditText>(R.id.txt_working_hours)
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

                val chainsawBucking = ChainsawBuckingDto()
                chainsawBucking.location = txtLocation.text.toString()
                chainsawBucking.areaSize = txtAreaSize.text.toString().toFloat()
                chainsawBucking.quantityOfChainsawMen =
                    Integer.valueOf(txtQuantityChainsawMen.text.toString()).toInt()
                chainsawBucking.workingHours = txtWorkingHours.text.toString().toFloat()
                chainsawBucking.gang = groups[spinner.selectedItemPosition].descriptionPrimary

                val appoint = AppointmentSemiMechanized().apply {

                    this.origin = "Semi_Mechanized"
                    this.type = "chainsaw_bucking"
                    this.data = chainsawBucking.toJson()
                    this.userId = userId
                    // collectedAt - add auto in repository
//                    this.collectedAt = OffsetDateTime.now()
                }

                val appointSemiMechanizedService =
                    AppointmentSemiMechanizedViewModel(requireActivity().application)

                appointSemiMechanizedService.save(appoint)

                /*var machine = Machine()
                machine.description = "SECURITY_NETWORK"
                machine.hotspotPassword = "picapausapoanoesgalinha111"
                machine.hotspotSsid="SECURITY_NETWORK"
                var machineService = MachineViewModel(requireActivity().application)
                machineService.save(machine)
*/
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
