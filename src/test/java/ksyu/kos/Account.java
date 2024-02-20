package ksyu.kos;

import java.util.List;

public class Account {

    private String id;
    private List<RemoteAccounts> remote_accounts;
    private String status;
    private Transactions transactions;

    public String getId() {
        return id;
    }

    public List<RemoteAccounts> getRemote_accounts() {
        return remote_accounts;
    }

    public String getStatus() {
        return status;
    }

    public Transactions getTransactions() {
        return transactions;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setRemote_accounts(List<RemoteAccounts> remote_accounts) {
        this.remote_accounts = remote_accounts;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTransactions(Transactions transactions) {
        this.transactions = transactions;
    }
}

