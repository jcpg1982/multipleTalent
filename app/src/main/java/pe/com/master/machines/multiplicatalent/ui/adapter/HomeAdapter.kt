package pe.com.master.machines.multiplicatalent.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import pe.com.master.machines.multiplicatalent.data.model.Data
import pe.com.master.machines.multiplicatalent.databinding.RowHomeBinding


class HomeAdapter(
    val loadImage: (url: String, imageV: ImageView) -> Unit,
    val openDetail: (data: Data, imageV: ImageView) -> Unit
) : RecyclerView.Adapter<HomeAdapter.viewHolder>() {

    private val TAG = HomeAdapter::class.java.simpleName

    private var mDataList: MutableList<Data> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder =
        viewHolder(RowHomeBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false))

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val item: Data = mDataList.get(position);
        holder.bind(item);
    }

    override fun getItemCount(): Int {
        return mDataList.size
    }

    fun setData(dataList: List<Data>) {
        if (mDataList == null) mDataList = ArrayList()
        mDataList.clear()
        mDataList.addAll(dataList)
        notifyDataSetChanged()
    }

    fun addData(dataList: List<Data>) {
        mDataList.addAll(dataList)
        notifyDataSetChanged()
    }

    inner class viewHolder(binding: RowHomeBinding) : RecyclerView.ViewHolder(binding.getRoot()),
        View.OnClickListener {
        private val binding: RowHomeBinding
        private var data: Data? = null

        fun bind(item: Data) {
            data = item
            data?.let {
                loadImage(it.album.cover_big, binding.imageAlbum)
                binding.txtName.text = it.title
            }
        }


        override fun onClick(view: View) {
            when (view.getId()) {
                binding.containerRow.id -> {
                    data?.let {
                        openDetail(it, binding.imageAlbum)
                    }
                }
            }
        }

        init {
            this.binding = binding
            binding.containerRow.setOnClickListener(this)
        }
    }
}