package uz.artikov.modul_6_lesson_3_1

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.navigation.findNavController
import uz.artikov.modul_6_lesson_3_1.adapters.RvAdapters
import uz.artikov.modul_6_lesson_3_1.adapters.SpinnerAdapter
import uz.artikov.modul_6_lesson_3_1.databinding.FragmentSecondBinding
import uz.artikov.modul_6_lesson_3_1.databinding.ItemCustomDialogBinding
import uz.artikov.modul_6_lesson_3_1.db.MyDbHelper
import uz.artikov.modul_6_lesson_3_1.models.Bootcamp

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [secondFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class secondFragment : Fragment() {

    private var _binding:FragmentSecondBinding?=null
    private val binding get() = _binding!!
    lateinit var root:View
    lateinit var rvAdapters: RvAdapters
    lateinit var spinnerAdapter: SpinnerAdapter
    lateinit var socialList:ArrayList<Bootcamp>
    lateinit var spinnerSocialList:ArrayList<String>
    lateinit var myDbHelper: MyDbHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentSecondBinding.inflate(inflater,container,false)
        root=binding.root

        myDbHelper= MyDbHelper(root.context)

        setHasOptionsMenu(true)

        spinnerSocialList= ArrayList()
        spinnerSocialList.add("Asosiy")
        spinnerSocialList.add("Dunyo")
        spinnerSocialList.add("Ijtimoiy")

        socialList= ArrayList()

        val allBasic = myDbHelper.getAllBasic()
        for (basic in allBasic) {
            if (basic.type==spinnerSocialList[2]){
                socialList.add(basic)
            }
        }

        rvAdapters= RvAdapters(socialList)
        binding.socialRv.adapter=rvAdapters
        rvAdapters.notifyItemInserted(socialList.size)
        rvAdapters.notifyItemChanged(socialList.size)


        rvAdapters.setMyItemOnClick(object :RvAdapters.MyItemOnClickListener{
            override fun onItemClick(basic: Bootcamp, position: Int) {
                var bundle=Bundle()
                bundle.putSerializable("basic",basic)
                bundle.putInt("pos",position)
                bundle.putInt("position",2)
                root.findNavController().navigate(R.id.fourthFragment,bundle)
            }
        })

        rvAdapters.setMyImgOnClick(object :RvAdapters.MyImgItemOnClickListener{
            override fun onMyItemClick(basic: Bootcamp, position: Int,imageView: ImageView) {
                val popupMenu= PopupMenu(root.context,imageView)
                popupMenu.inflate(R.menu.pop_menu)
                popupMenu.setOnMenuItemClickListener {
                    when(it.itemId){
                        R.id.edit->{
                            val alertDialog=AlertDialog.Builder(root.context,R.style.SheetDialog)
                            val dialog = alertDialog.create()
                            val dialogView= ItemCustomDialogBinding.inflate(LayoutInflater.from(root.context),null,false)
                            dialogView.itemTitleTv.text="O'zgartirish"
                            spinnerAdapter= SpinnerAdapter(spinnerSocialList)
                            dialogView.spinnerDialog.adapter=spinnerAdapter
                            dialogView.nameEt.setText(basic.name)
                            dialogView.descriptionEt.setText(basic.description)
                            dialogView.spinnerDialog.setSelection(2)

                            dialogView.saveText.setOnClickListener {
                                val type = spinnerSocialList[dialogView.spinnerDialog.selectedItemPosition]
                                val name = dialogView.nameEt.text.toString().trim()
                                val descriptions = dialogView.descriptionEt.text.toString().trim()

                                if (name.isNotEmpty() && type.isNotEmpty() && descriptions.isNotEmpty()){

                                    basic.type=spinnerSocialList[dialogView.spinnerDialog.selectedItemPosition]
                                    basic.name=dialogView.nameEt.text.toString().trim()
                                    basic.description=dialogView.descriptionEt.text.toString().trim()

                                    myDbHelper.updateBasic(basic)
                                    socialList[position] = basic

                                    socialList.clear()

                                    val allBasic = myDbHelper.getAllBasic()
                                    for (basic in allBasic) {
                                        if (basic.type==spinnerSocialList[2]){
                                            socialList.add(basic)
                                        }
                                    }

                                    binding.socialRv.adapter = rvAdapters
                                    rvAdapters.notifyItemInserted(socialList.size)
                                    rvAdapters.notifyItemChanged(position)
                                    rvAdapters.notifyItemRangeInserted(position, socialList.size)
                                    rvAdapters.notifyItemRangeChanged(position, socialList.size)

                                    dialog.dismiss()
                                    Toast.makeText(root.context, "O'zgartirildi", Toast.LENGTH_SHORT).show()
                                }else{
                                    Toast.makeText(root.context, "Iltimos bo'sh maydonlarni to'ldiring", Toast.LENGTH_SHORT).show()
                                }
                            }
                            dialogView.notText.setOnClickListener {
                                dialog.dismiss()
                            }
                            dialog.setView(dialogView.root)
                            dialog.show()

                        }
                        R.id.delete->{
                            val alertDialog= AlertDialog.Builder(root.context)
                            alertDialog.setMessage("Xabarni o'chirmoqchimisiz?")
                            alertDialog.setPositiveButton("O'chirish",object : DialogInterface.OnClickListener{
                                override fun onClick(p0: DialogInterface?, p1: Int) {
                                    myDbHelper.deleteBasic(basic)
                                    socialList.remove(basic)
                                    rvAdapters.notifyItemRemoved(position)
                                    rvAdapters.notifyItemRangeChanged(position,socialList.size)
                                }
                            })
                            alertDialog.setNegativeButton("Bekor qilish",object : DialogInterface.OnClickListener{
                                override fun onClick(p0: DialogInterface?, p1: Int) {

                                }
                            })
                            alertDialog.show()
                        }
                    }
                    true
                }
                popupMenu.show()
            }
        })

        return root
    }
    override fun onResume() {
        super.onResume()
        socialList.clear()
        var allBasic = myDbHelper.getAllBasic()

        for (basic in allBasic) {
            if (basic.type == spinnerSocialList[2]) {
                socialList.add(basic)
            }
        }

        rvAdapters.notifyDataSetChanged()
    }
}