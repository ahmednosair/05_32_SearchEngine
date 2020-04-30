package eg.edu.alexu.csd.filestructure.btree;

import java.util.Objects;

public class SearchResult implements ISearchResult {
    private String id;
    private int rank;

    public SearchResult(String id, int rank) {
        this.id = id;
        this.rank = rank;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int getRank() {
        return rank;
    }

    @Override
    public void setRank(int rank) {
        this.rank = rank;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SearchResult that = (SearchResult) o;
        if (rank != that.rank) return false;
        return id.equals(that.id);
    }


}
