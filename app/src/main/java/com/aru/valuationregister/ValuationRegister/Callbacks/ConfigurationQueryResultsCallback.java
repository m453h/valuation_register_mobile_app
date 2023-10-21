package com.aru.valuationregister.ValuationRegister.Callbacks;

import com.aru.valuationregister.Database.Models.Configuration;

public interface ConfigurationQueryResultsCallback {
    void onDataReceived(Configuration configuration, String action);
}
