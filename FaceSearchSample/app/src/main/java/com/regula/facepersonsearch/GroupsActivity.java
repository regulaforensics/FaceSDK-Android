package com.regula.facepersonsearch;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.regula.facepersonsearch.databinding.ActivityItemsListBinding;
import com.regula.facesdk.FaceSDK;
import com.regula.facesdk.callback.PersonDBCallback;
import com.regula.facesdk.model.results.personDb.PageableItemList;
import com.regula.facesdk.model.results.personDb.PersonGroup;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("NotifyDataSetChanged")
public class GroupsActivity extends AppCompatActivity {
    public static final String GROUP_ID = "groupId";

    ActivityItemsListBinding binding;
    List<PersonGroup> groups = new ArrayList<>();

    ItemsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityItemsListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        registerForContextMenu(binding.itemsList);
    }

    @Override
    protected void onResume() {
        super.onResume();

        adapter = new GroupsAdapter(groups, onItemClick);
        binding.itemsList.setLayoutManager(new LinearLayoutManager(GroupsActivity.this));
        binding.itemsList.addItemDecoration(
                new DividerItemDecoration(GroupsActivity.this, DividerItemDecoration.VERTICAL));
        binding.itemsList.setAdapter(adapter);

        FaceSDK.Instance().personDatabase().getGroups(new PersonDBCallback<PageableItemList<List<PersonGroup>, PersonGroup>>() {
            @Override
            public void onSuccess(@Nullable PageableItemList<List<PersonGroup>, PersonGroup> response) {
                if(response == null)
                    return;

                binding.createBtn.setVisibility(View.VISIBLE);
                binding.searchBtn.setVisibility(View.VISIBLE);
                binding.itemsList.setVisibility(View.VISIBLE);

                groups.clear();;
                groups.addAll(response.getItemsList());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(@NonNull String message) {
                Toast.makeText(GroupsActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });

        binding.createBtn.setOnClickListener(view -> {
            new AlertDialog.Builder(GroupsActivity.this)
                    .setCancelable(true)
                    .setTitle("CREATE GROUP")
                    .setView(R.layout.group_create_update)
                    .setPositiveButton("CREATE", (dialogInterface, i) -> {
                        EditText editText = ((AlertDialog) dialogInterface).findViewById(R.id.groupNameTV);
                        String groupName = editText.getText().toString();
                        FaceSDK.Instance().personDatabase().createGroup(groupName, new PersonDBCallback<PersonGroup>() {
                            @Override
                            public void onSuccess(@Nullable PersonGroup response) {
                                Toast.makeText(GroupsActivity.this, "Success!", Toast.LENGTH_SHORT).show();
                                groups.add(response);
                                adapter.notifyItemInserted(groups.size());
                                dialogInterface.dismiss();
                            }

                            @Override
                            public void onFailure(@NonNull String message) {
                                Toast.makeText(GroupsActivity.this, "Failed: " + message, Toast.LENGTH_SHORT).show();
                                dialogInterface.dismiss();
                            }
                        });
                    })
                    .setNegativeButton("CANCEL", (dialogInterface, i) -> {
                        dialogInterface.cancel();
                    }).show();
        });

        binding.searchBtn.setOnClickListener(view -> {
            Intent searchIntent = new Intent(GroupsActivity.this, SearchActivity.class);
            String[] groupIds = new String[groups.size()];
            int i = 0;
            for(PersonGroup group : groups){
                groupIds[i] = group.getId();
                i++;
            }
            searchIntent.putExtra(GROUP_ID, groupIds);
            startActivity(searchIntent);
        });
    }

    View.OnClickListener onItemClick = view -> {
        int itemPosition = -1;
        try {
            itemPosition = ((ItemsAdapter)binding.itemsList.getAdapter()).getPosition();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(GroupsActivity.this, PersonsActivity.class);
        PersonGroup groupEdit = groups.get(itemPosition);
        intent.putExtra(GROUP_ID, groupEdit.getId());
         startActivity(intent);
    };

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int itemPosition = -1;
        try {
            itemPosition = ((ItemsAdapter)binding.itemsList.getAdapter()).getPosition();
        } catch (Exception e) {
            e.printStackTrace();
            return super.onContextItemSelected(item);
        }

        Log.d("DEBUG", "Item position: " + itemPosition  + " groups: " + groups.size());
        PersonGroup groupEdit = groups.get(itemPosition);
        int finalItemPosition = itemPosition;

        switch (item.getItemId()) {
            case ItemsAdapter.MENU_EDIT:
                AlertDialog dialog = new AlertDialog.Builder(GroupsActivity.this)
                        .setCancelable(true)
                        .setTitle("EDIT GROUP")
                        .setView(R.layout.group_create_update)
                        .setPositiveButton("EDIT", (dialogInterface, i) -> {
                            EditText editText = ((AlertDialog) dialogInterface).findViewById(R.id.groupNameTV);
                            String groupName = editText.getText().toString();
                            groupEdit.setName(groupName);
                            FaceSDK.Instance().personDatabase().updateGroup(groupEdit, new PersonDBCallback<Void>() {
                                @Override
                                public void onSuccess(@Nullable Void response) {
                                    Toast.makeText(GroupsActivity.this, "Success!", Toast.LENGTH_SHORT).show();
                                    adapter.notifyItemChanged(finalItemPosition);
                                    dialogInterface.dismiss();
                                }

                                @Override
                                public void onFailure(@NonNull String message) {
                                    Toast.makeText(GroupsActivity.this, "Failed: " + message, Toast.LENGTH_SHORT).show();
                                    dialogInterface.dismiss();
                                }
                            });
                        })
                        .setNegativeButton("CANCEL", (dialogInterface, i) -> {
                            dialogInterface.cancel();
                        }).show();
                EditText editText = dialog.findViewById(R.id.groupNameTV);
                editText.setText(groupEdit.getName());
                break;
            case ItemsAdapter.MENU_DELETE:
                FaceSDK.Instance().personDatabase().deleteGroup(groupEdit.getId(), new PersonDBCallback<Void>() {
                    @Override
                    public void onSuccess(@Nullable Void response) {
                        Toast.makeText(GroupsActivity.this, "Success!", Toast.LENGTH_SHORT).show();
                        groups.remove(finalItemPosition);
                        adapter.notifyItemRemoved(finalItemPosition);
                    }

                    @Override
                    public void onFailure(@NonNull String message) {
                        Toast.makeText(GroupsActivity.this, "Failed: " + message, Toast.LENGTH_SHORT).show();
                    }
                });
                break;
        }
        return super.onContextItemSelected(item);
    }

    public static class GroupsAdapter extends ItemsAdapter {

        private final List<PersonGroup> groups;

        public GroupsAdapter(List<PersonGroup> groups, View.OnClickListener onItemClickListener) {
            super(onItemClickListener);
            this.groups = groups;
        }


        @Override
        public void onBindViewHolder(@NonNull ItemsViewHolder holder, int position) {
            super.onBindViewHolder(holder, position);

            PersonGroup group = groups.get(position);

            holder.id.setText(group.getId());
            holder.name.setText(group.getName());
        }

        @Override
        public int getItemCount() {
            return groups.size();
        }
    }
}