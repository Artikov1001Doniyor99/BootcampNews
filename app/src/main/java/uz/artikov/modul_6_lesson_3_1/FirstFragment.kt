package uz.artikov.modul_6_lesson_3_1

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import uz.artikov.modul_6_lesson_3_1.adapters.RvAdapters
import uz.artikov.modul_6_lesson_3_1.adapters.SpinnerAdapter
import uz.artikov.modul_6_lesson_3_1.databinding.FragmentFirstBinding
import uz.artikov.modul_6_lesson_3_1.databinding.ItemCustomDialogBinding
import uz.artikov.modul_6_lesson_3_1.db.MyDbHelper
import uz.artikov.modul_6_lesson_3_1.models.Bootcamp

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FirstFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!
    lateinit var root: View
    lateinit var rvAdapters: RvAdapters
    lateinit var spinnerAdapter: SpinnerAdapter
    lateinit var basicList: ArrayList<Bootcamp>
    lateinit var spinnerBasicList: ArrayList<String>
    lateinit var myDbHelper: MyDbHelper
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        root = binding.root
        myDbHelper = MyDbHelper(root.context)
        setHasOptionsMenu(true)

        spinnerBasicList = ArrayList()
        spinnerBasicList.add("Asosiy")
        spinnerBasicList.add("Dunyo")
        spinnerBasicList.add("Ijtimoiy")

        basicList = ArrayList()

        var allBasic = myDbHelper.getAllBasic()

        for (basic in allBasic) {
            if (basic.type == spinnerBasicList[0]) {
                basicList.add(basic)
            }
        }

        rvAdapters = RvAdapters(basicList)
        binding.basicRv.adapter = rvAdapters
        rvAdapters.notifyItemInserted(basicList.size)
        rvAdapters.notifyItemChanged(basicList.size)

        rvAdapters.setMyItemOnClick(object : RvAdapters.MyItemOnClickListener {
            override fun onItemClick(basic: Bootcamp, position: Int) {
                var bundle = Bundle()
                bundle.putSerializable("basic", basic)
                bundle.putInt("pos", position)
                bundle.putInt("position", 0)
                root.findNavController().navigate(R.id.fourthFragment, bundle)
            }
        })

        rvAdapters.setMyImgOnClick(object : RvAdapters.MyImgItemOnClickListener {
            override fun onMyItemClick(basic: Bootcamp, position: Int, imageView: ImageView) {
                val popupMenu = PopupMenu(root.context, imageView)
                popupMenu.inflate(R.menu.pop_menu)
                popupMenu.setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.edit -> {
                            val alertDialog = AlertDialog.Builder(root.context, R.style.SheetDialog)
                            val dialog = alertDialog.create()
                            val dialogView = ItemCustomDialogBinding.inflate(
                                LayoutInflater.from(root.context),
                                null,
                                false
                            )
                            dialogView.itemTitleTv.text = "O'zgartirish"
                            spinnerAdapter = SpinnerAdapter(spinnerBasicList)
                            dialogView.spinnerDialog.adapter = spinnerAdapter

                            dialogView.nameEt.setText(basic.name)
                            dialogView.descriptionEt.setText(basic.description)
                            dialogView.spinnerDialog.setSelection(0)

                            dialogView.saveText.setOnClickListener {

                                val type =
                                    spinnerBasicList[dialogView.spinnerDialog.selectedItemPosition]
                                val name = dialogView.nameEt.text.toString().trim()
                                val descriptions = dialogView.descriptionEt.text.toString().trim()

                                if (name.isNotEmpty() && type.isNotEmpty() && descriptions.isNotEmpty()) {
                                    basic.type =
                                        spinnerBasicList[dialogView.spinnerDialog.selectedItemPosition]
                                    basic.name = dialogView.nameEt.text.toString().trim()
                                    basic.description =
                                        dialogView.descriptionEt.text.toString().trim()

                                    myDbHelper.updateBasic(basic)
                                    basicList[position] = basic

                                    basicList.clear()

                                    var allBasic = myDbHelper.getAllBasic()

                                    for (basic in allBasic) {
                                        if (basic.type == spinnerBasicList[0]) {
                                            basicList.add(basic)
                                        }
                                    }

                                   // rvAdapters = RvAdapters(basicList)
                                    binding.basicRv.adapter = rvAdapters
                                    rvAdapters.notifyItemInserted(basicList.size)
                                    rvAdapters.notifyItemChanged(position)
                                    rvAdapters.notifyItemRangeInserted(position, basicList.size)
                                    rvAdapters.notifyItemRangeChanged(position, basicList.size)

                                    dialog.dismiss()
                                    Toast.makeText(
                                        root.context,
                                        "O'zgartirildi",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    Toast.makeText(
                                        root.context,
                                        "Iltimos bo'sh maydonlarni to'ldiring",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                            dialogView.notText.setOnClickListener {
                                dialog.dismiss()
                            }
                            dialog.setView(dialogView.root)
                            dialog.show()

                        }
                        R.id.delete -> {
                            val alertDialog = AlertDialog.Builder(root.context)
                            alertDialog.setMessage("Xabarni o'chirmoqchimisiz?")
                            alertDialog.setPositiveButton(
                                "O'chirish"
                            ) { p0, p1 ->
                                myDbHelper.deleteBasic(basic)
                                basicList.remove(basic)
                                rvAdapters.notifyItemRemoved(position)
                                rvAdapters.notifyItemRangeChanged(position, basicList.size)
                            }
                            alertDialog.setNegativeButton(
                                "Bekor qilish"
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
        basicList.clear()
        var allBasic = myDbHelper.getAllBasic()

        for (basic in allBasic) {
            if (basic.type == spinnerBasicList[0]) {
                basicList.add(basic)
            }
        }

        rvAdapters.notifyDataSetChanged()
    }


}