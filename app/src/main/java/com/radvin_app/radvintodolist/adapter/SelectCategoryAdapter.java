package com.radvin_app.radvintodolist.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;
import com.radvin_app.R;
import com.radvin_app.radvintodolist.storage.Category;

import java.util.ArrayList;
import java.util.List;

public class SelectCategoryAdapter extends RecyclerView.Adapter<SelectCategoryAdapter.CategoryViewHolder> {
    private List<Category> categoryList = new ArrayList<>();


    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoryViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        holder.bindCategory(categoryList.get(position));
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public void addCategory(Category category){
        categoryList.add(0,category);
        notifyItemInserted(0);
    }

    public void addCategories(List<Category> categoryList){
        this.categoryList.addAll(categoryList);
        notifyDataSetChanged();
    }

    public void deleteCategory(Category category){
        for (int i = 0; i <categoryList.size() ; i++) {
            if (categoryList.get(i).getId()==category.getId()){
                categoryList.remove(i);
                notifyItemRemoved(i);
                break;
            }
        }
    }

    public void updateItemCategory(Category category){
        for (int i = 0; i < categoryList.size(); i++) {
            if (category.getId() == categoryList.get(i).getId()) {
                categoryList.set(i, category);
                notifyItemChanged(i);
            }
        }
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder{
        private MaterialTextView tvCategoryName, tvCategoryFirstCharacter;


        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategoryFirstCharacter = itemView.findViewById(R.id.tvCategoryFirstCharacter);
            tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
        }

        public void bindCategory(final Category category){
            tvCategoryName.setText(category.getName());
            tvCategoryFirstCharacter.setText(category.getName().substring(0,1));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   // eventListener.onItemClickPress(category);
                }
            });
        }
    }


}
