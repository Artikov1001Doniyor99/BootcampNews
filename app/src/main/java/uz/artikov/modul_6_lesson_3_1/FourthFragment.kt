package uz.artikov.modul_6_lesson_3_1

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SpinnerAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import uz.artikov.modul_6_lesson_3_1.databinding.FragmentFourthBinding
import uz.artikov.modul_6_lesson_3_1.databinding.ItemCustomDialogBinding
import uz.artikov.modul_6_lesson_3_1.db.MyDbHelper
import uz.artikov.modul_6_lesson_3_1.models.Bootcamp

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FourthFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FourthFragment : Fragment() {
    private var _binding: FragmentFourthBinding?=null
    private val binding get() = _binding!!
    lateinit var spinnerAdapter: SpinnerAdapter
    lateinit var myDbHelper: MyDbHelper
    lateinit var list: ArrayList<String>
    private lateinit var root:View
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentFourthBinding.inflate(inflater,container,false)
        root=binding.root

        myDbHelper= MyDbHelper(root.context)

        val basic = arguments?.getSerializable("basic") as Bootcamp
        val position = arguments?.getInt("pos")

        val pos = arguments?.getInt("position")


        list= ArrayList()

        list.add("Asosiy")
        list.add("Dunyo")
        list.add("Ijtimoiy")

        val allBasic = myDbHelper.getAllBasic()

        binding.tv1.text=basic.name
        binding.tv2.text=basic.description

        binding.back.setOnClickListener {
            root.findNavController().popBackStack()
        }

        binding.floating.setOnClickListener {
            val alertDialog= AlertDialog.Builder(root.context,R.style.SheetDialog)
            val dialog = alertDialog.create()
            val dialogView= ItemCustomDialogBinding.inflate(LayoutInflater.from(root.context),null,false)
            dialogView.itemTitleTv.text="O'zgartirish"

            spinnerAdapter= uz.artikov.modul_6_lesson_3_1.adapters.SpinnerAdapter(list)
            dialogView.spinnerDialog.adapter=spinnerAdapter

            dialogView.nameEt.setText(basic.name)
            dialogView.descriptionEt.setText(basic.description)
            dialogView.spinnerDialog.setSelection(pos!!)


            dialogView.saveText.setOnClickListener {
                val type = list[dialogView.spinnerDialog.selectedItemPosition]
                val name = dialogView.nameEt.text.toString().trim()
                val descriptions = dialogView.descriptionEt.text.toString().trim()

                if (name.isNotEmpty() && type.isNotEmpty() && descriptions.isNotEmpty()){


                    basic.type=list[dialogView.spinnerDialog.selectedItemPosition]
                    basic.name=dialogView.nameEt.text.toString().trim()
                    basic.description=dialogView.descriptionEt.text.toString().trim()

                    myDbHelper.updateBasic(basic)
                    allBasic[position!!] = basic

                    binding.tv1.text = "${basic.name}"
                    binding.tv2.text = "${basic.description}"

                    dialog.dismiss()
                    Toast.makeText(root.context, "Changed", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(root.context, "Fill in the blanks", Toast.LENGTH_SHORT).show()
                }
            }
            dialogView.notText.setOnClickListener {
                dialog.dismiss()
            }
            dialog.setView(dialogView.root)
            dialog.show()
        }

        return root
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
    }
}