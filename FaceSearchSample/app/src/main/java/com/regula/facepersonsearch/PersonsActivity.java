package com.regula.facepersonsearch;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.regula.facepersonsearch.databinding.ActivityItemsListBinding;
import com.regula.facesdk.FaceSDK;
import com.regula.facesdk.callback.PersonDBCallback;
import com.regula.facesdk.model.results.person.PageableItemList;
import com.regula.facesdk.model.results.person.Person;
import com.regula.facesdk.request.person.EditGroupPersonsRequest;

import java.util.ArrayList;
import java.util.List;

public class PersonsActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityItemsListBinding binding;
    List<Person> personsInGroup = new ArrayList<>();

    String groupId;
    ItemsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityItemsListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        registerForContextMenu(binding.itemsList);

        groupId = getIntent().getStringExtra(GroupsActivity.GROUP_ID);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(groupId == null)
        {
            //TODO
        }

        adapter = new PersonsAdapter(personsInGroup, this);
        binding.itemsList.setLayoutManager(new LinearLayoutManager(PersonsActivity.this));
        binding.itemsList.addItemDecoration(
                new DividerItemDecoration(PersonsActivity.this, DividerItemDecoration.VERTICAL));
        binding.itemsList.setAdapter(adapter);

        FaceSDK.Instance().personDatabase().getPersonsInGroup(groupId, new PersonDBCallback<PageableItemList<List<Person>, Person>>() {
            @Override
            public void onSuccess(@Nullable PageableItemList<List<Person>, Person> response) {
                if(response == null)
                    return;

                binding.createBtn.setVisibility(View.VISIBLE);
                binding.searchBtn.setVisibility(View.VISIBLE);
                binding.itemsList.setVisibility(View.VISIBLE);

                personsInGroup.clear();;
                personsInGroup.addAll(response.getItemsList());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(@NonNull String message) {
                Toast.makeText(PersonsActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });

        binding.createBtn.setOnClickListener(view -> {
            Intent intent = new Intent(PersonsActivity.this, CreateActivity.class);
            intent.putExtra(GroupsActivity.GROUP_ID, groupId);
            startActivity(intent);
        });

        binding.searchBtn.setOnClickListener(view -> {
            Intent intent = new Intent(PersonsActivity.this, SearchActivity.class);
            intent.putExtra(GroupsActivity.GROUP_ID, new String[]{groupId});
            startActivity(intent);
        });
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int itemPosition = -1;
        try {
            itemPosition = ((ItemsAdapter)binding.itemsList.getAdapter()).getPosition();
        } catch (Exception e) {
            e.printStackTrace();
            return super.onContextItemSelected(item);
        }

        Person personEdit = personsInGroup.get(itemPosition);
        int finalItemPosition = itemPosition;

        switch (item.getItemId()) {
            case ItemsAdapter.MENU_DELETE:
                FaceSDK.Instance().personDatabase().deletePerson(personEdit.getId(), new PersonDBCallback<Void>() {
                    @Override
                    public void onSuccess(@Nullable Void response) {
                        Toast.makeText(PersonsActivity.this, "Success!", Toast.LENGTH_SHORT).show();
                        personsInGroup.remove(finalItemPosition);
                        adapter.notifyItemRemoved(finalItemPosition);
                    }

                    @Override
                    public void onFailure(@NonNull String message) {
                        Toast.makeText(PersonsActivity.this, "Failed: " + message, Toast.LENGTH_SHORT).show();
                    }
                });
                break;
                case ItemsAdapter.MENU_DELETE_FROM_GROUP:
                    EditGroupPersonsRequest request = new EditGroupPersonsRequest();
                    request.setPersonIdsToRemove(new String[]{personEdit.getId()});
                    FaceSDK.Instance().personDatabase().editPersonsInGroup(groupId, request, new PersonDBCallback<Void>() {
                        @Override
                        public void onSuccess(@Nullable Void response) {
                            Toast.makeText(PersonsActivity.this, "Success!", Toast.LENGTH_SHORT).show();
                            personsInGroup.remove(finalItemPosition);
                            adapter.notifyItemRemoved(finalItemPosition);
                        }

                        @Override
                        public void onFailure(@NonNull String message) {
                            Toast.makeText(PersonsActivity.this, "Failed: " + message, Toast.LENGTH_SHORT).show();
                        }
                    });
                    break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        int itemPosition = -1;
        try {
            itemPosition = ((ItemsAdapter)binding.itemsList.getAdapter()).getPosition();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Person personEdit = personsInGroup.get(itemPosition);

        Intent intent = new Intent(PersonsActivity.this, CreateActivity.class);
        intent.putExtra(GroupsActivity.GROUP_ID, groupId);
        intent.putExtra(CreateActivity.PERSON_ID, personEdit.getId());
        startActivity(intent);
    }

    static class PersonsAdapter extends ItemsAdapter {

        List<Person> persons;

        public PersonsAdapter(@NonNull List<Person> persons, View.OnClickListener onItemClickListener) {
            super(onItemClickListener);
            this.persons = persons;
        }


        @NonNull
        @Override
        public ItemsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new PersonsViewHolder(
                    LayoutInflater.from(parent.getContext())
                            .inflate(android.R.layout.two_line_list_item, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ItemsViewHolder ih, int position) {
            super.onBindViewHolder(ih, position);

            PersonsViewHolder holder = (PersonsViewHolder)ih;

            Person group = persons.get(position);

            holder.id.setText(group.getId());
            holder.name.setText(group.getName());
        }

        @Override
        public int getItemCount() {
            return persons.size();
        }

        class PersonsViewHolder extends ItemsViewHolder {

            public PersonsViewHolder(@NonNull View itemView) {
                super(itemView);
            }

            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

                menu.setHeaderTitle("Select The Action");
                menu.add(0, ItemsAdapter.MENU_DELETE_FROM_GROUP, 0, "REMOVE FROM GROUP");
                menu.add(0, ItemsAdapter.MENU_DELETE, 0, "DELETE");
            }
        }
    }
}
