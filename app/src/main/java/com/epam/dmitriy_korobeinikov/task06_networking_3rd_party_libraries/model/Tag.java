package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.model;

import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.content.OwnerContent;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.content.TagContent;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created by Dmitriy Korobeynikov on 09.01.2015.
 * Contains data about repository's tags and describes database table's fields.
 */
@DatabaseTable(tableName = TagContent.TABLE_NAME)
public class Tag {

    @DatabaseField(columnName = TagContent._ID, generatedId = true)
    public int id;

    @DatabaseField(columnName = TagContent.REPOSITORY_TAGS)
    public String repositoryTags;
}
