package uz.artikov.modul_6_lesson_3_1

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.viewpager.widget.ViewPager
import uz.artikov.modul_6_lesson_3_1.adapters.SpinnerAdapter
import uz.artikov.modul_6_lesson_3_1.adapters.ViewPagerAdapters
import uz.artikov.modul_6_lesson_3_1.databinding.FragmentHomeBinding
import uz.artikov.modul_6_lesson_3_1.databinding.ItemCustomDialogBinding
import uz.artikov.modul_6_lesson_3_1.db.MyDbHelper
import uz.artikov.modul_6_lesson_3_1.models.Bootcamp
import android.widget.SpinnerAdapter as SpinnerAdapter1

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding?=null
    private val binding get() = _binding!!
    lateinit var root:View
    lateinit var spinnerAdapter: SpinnerAdapter1
    lateinit var list: ArrayList<String>
    lateinit var viewPagerAdapters: ViewPagerAdapters
    lateinit var myDbHelper: MyDbHelper
    var positionViewPager = -1
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding= FragmentHomeBinding.inflate(inflater,container,false)
        root=binding.root
        myDbHelper = MyDbHelper(root.context)
        setHasOptionsMenu(true)

        list = ArrayList()

        list.add("Asosiy")
        list.add("Dunyo")
        list.add("Ijtimoiy")

        viewPagerAdapters = ViewPagerAdapters(list, childFragmentManager)
        binding.viewPager.adapter = viewPagerAdapters
        binding.tabLayout.setupWithViewPager(binding.viewPager)

        viewPagerAdapters.notifyDataSetChanged()

        binding.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                positionViewPager=position
            }

            override fun onPageSelected(position: Int) {

            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })

        return root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.add -> {
                var alertDialog = androidx.appcompat.app.AlertDialog.Builder(root.context, R.style.SheetDialog)
                val dialog = alertDialog.create()
                val dialogView =
                    ItemCustomDialogBinding.inflate(LayoutInflater.from(root.context), null, false)

                spinnerAdapter = SpinnerAdapter(list)
                dialogView.spinnerDialog.adapter = spinnerAdapter

                dialogView.saveText.setOnClickListener {
                    val type = list[dialogView.spinnerDialog.selectedItemPosition]
                    val name = dialogView.nameEt.text.toString().trim()
                    val description = dialogView.descriptionEt.text.toString().trim()
                    if (type.isNotEmpty() && name.isNotEmpty() && description.isNotEmpty()) {
                        val basic = Bootcamp(type, name, description)
                        myDbHelper.addBasic(basic)
                        dialogView.nameEt.setText("")
                        dialogView.descriptionEt.setText("")
                        dialog.dismiss()
                        viewPagerAdapters = ViewPagerAdapters(list, childFragmentManager)
                        binding.viewPager.adapter = viewPagerAdapters
                        binding.tabLayout.setupWithViewPager(binding.viewPager)
                        binding.viewPager.currentItem=positionViewPager
                        Toast.makeText(root.context, "Saved", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(root.context, "Fill in the blanks", Toast.LENGTH_SHORT).show()
                    }
                }
                dialogView.notText.setOnClickListener {
                    dialog.dismiss()
                }
                dialog.setView(dialogView.root)
                dialog.show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

}