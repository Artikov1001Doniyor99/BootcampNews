package uz.mobiler.bootcamp

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.navigation.findNavController
import uz.artikov.modul_6_lesson_3_1.R
import uz.artikov.modul_6_lesson_3_1.adapters.RvAdapters
import uz.artikov.modul_6_lesson_3_1.adapters.SpinnerAdapter
import uz.artikov.modul_6_lesson_3_1.databinding.FragmentThirdBinding
import uz.artikov.modul_6_lesson_3_1.databinding.ItemCustomDialogBinding
import uz.artikov.modul_6_lesson_3_1.db.MyDbHelper
import uz.artikov.modul_6_lesson_3_1.models.Bootcamp

class ThirdFragment : Fragment() {

    private var _binding:FragmentThirdBinding?=null
    private val binding get() = _binding!!
    lateinit var root:View
    lateinit var rvAdapters: RvAdapters
    lateinit var spinnerAdapter: SpinnerAdapter
    lateinit var worldList:ArrayList<Bootcamp>
    lateinit var spinnerWorldList:ArrayList<String>
    lateinit var myDbHelper: MyDbHelper


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentThirdBinding.inflate(inflater,container,false)
        root=binding.root
        myDbHelper= MyDbHelper(root.context)
        setHasOptionsMenu(true)

        spinnerWorldList= ArrayList()
        spinnerWorldList.add("Asosiy")
        spinnerWorldList.add("Dunyo")
        spinnerWorldList.add("Ijtimoiy")

        worldList= ArrayList()

        val allBasic = myDbHelper.getAllBasic()

        for (basic in allBasic) {
            if (basic.type==spinnerWorldList[1]){
                worldList.add(basic)
            }
        }


        rvAdapters= RvAdapters(worldList)
        binding.worldRv.adapter=rvAdapters
        rvAdapters.notifyItemInserted(worldList.size)

        rvAdapters.setMyItemOnClick(object :RvAdapters.MyItemOnClickListener{
            override fun onItemClick(basic: Bootcamp, position: Int) {
                var bundle=Bundle()
                bundle.putSerializable("basic",basic)
                bundle.putInt("pos",position)
                bundle.putInt("position",1)
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

                            spinnerAdapter= SpinnerAdapter(spinnerWorldList)
                            dialogView.spinnerDialog.adapter=spinnerAdapter

                            dialogView.nameEt.setText(basic.name)
                            dialogView.descriptionEt.setText(basic.description)
                            dialogView.spinnerDialog.setSelection(1)

                            spinnerAdapter.notifyDataSetChanged()

                            dialogView.saveText.setOnClickListener {

                                val type = spinnerWorldList[dialogView.spinnerDialog.selectedItemPosition]
                                val name = dialogView.nameEt.text.toString().trim()
                                val descriptions = dialogView.descriptionEt.text.toString().trim()

                                if (name.isNotEmpty() && type.isNotEmpty() && descriptions.isNotEmpty()){

                                    basic.type=spinnerWorldList[dialogView.spinnerDialog.selectedItemPosition]
                                    basic.name=dialogView.nameEt.text.toString().trim()
                                    basic.description=dialogView.descriptionEt.text.toString().trim()

                                    myDbHelper.updateBasic(basic)
                                    worldList[position] = basic

                                    worldList.clear()

                                    val allBasic = myDbHelper.getAllBasic()

                                    for (basic in allBasic) {
                                        if (basic.type==spinnerWorldList[1]){
                                            worldList.add(basic)
                                        }
                                    }

/*                                    rvAdapters= RvAdapters(worldList)*/
                                    binding.worldRv.adapter=rvAdapters
                                    rvAdapters.notifyItemInserted(worldList.size)
                                    rvAdapters.notifyItemChanged(position)
                                    rvAdapters.notifyItemRangeInserted(position,worldList.size)
                                    rvAdapters.notifyItemRangeChanged(position,worldList.size)



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
                            alertDialog.setPositiveButton("O'chirish"
                            ) { p0, p1 ->
                                myDbHelper.deleteBasic(basic)
                                worldList.remove(basic)
                                rvAdapters.notifyItemRemoved(position)
                                rvAdapters.notifyItemRangeChanged(position, worldList.size)
                            }
                            alertDialog.setNegativeButton("Bekor qilish"
                            ) { p0, p1 -> }
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
        worldList.clear()
        var allBasic = myDbHelper.getAllBasic()

        for (basic in allBasic) {
            if (basic.type == spinnerWorldList[1]) {
                worldList.add(basic)
            }
        }

        rvAdapters.notifyDataSetChanged()
    }
}