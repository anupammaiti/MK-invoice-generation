const postData = (url = ``, data = {}) => {
    // Default options are marked with *
    return fetch(url, {
        method: "POST", // *GET, POST, PUT, DELETE, etc.
        mode: "cors", // no-cors, cors, *same-origin
        cache: "no-cache", // *default, no-cache, reload, force-cache, only-if-cached
        credentials: "same-origin", // include, same-origin, *omit
        headers: {
            "Content-Type": "application/json; charset=utf-8",
            // "Content-Type": "application/x-www-form-urlencoded",
        },
        redirect: "follow", // manual, *follow, error
        referrer: "no-referrer", // no-referrer, *client
        body: JSON.stringify(data), // body data type must match "Content-Type" header
    })
        .then(response => response.text()) // parses response to JSON
.catch(error => console.error(`Fetch Error =\n`, error));
};

/**
 * Will send value of objects back as json.
 * Each post request, will send the data to populate all object instances of
 * the required entities.
 */
document.addEventListener('DOMContentLoaded', function () {
    document.getElementById('submitButton').addEventListener('click', function () {
        this.disabled = true;
        let data = {
            invoiceType: document.getElementById('invoiceType').value,
            invoiceDate: new Date(document.getElementById('invoiceDate').value),
            invoiceNumber: document.getElementById('invoiceNumber').value,
            vatExempt: document.getElementById('vatExempt').value,
            reverseCharge: document.getElementById('reverseCharge').value,
            frequency: document.getElementById('frequency').value,
            period: document.getElementById('period').value,
            year: parseInt(document.getElementById('year').value),
            portfolio: parseInt(document.getElementById('portfolio').value),
            vatApplicable: document.getElementById('vatApplicable').value,
            vat: parseInt(document.getElementById('vat').value),
            serviceProvided: parseInt(document.getElementById('serviceProvided').value),
            bankAccount: parseInt(document.getElementById('bankAccount').value),
            fromCurrency: parseInt(document.getElementById('fromCurrency').value),
            toCurrency: parseInt(document.getElementById('toCurrency').value),
            baseCharge: parseFloat(document.getElementById('baseCharge').value)
        };


        /*
        Sending the first post request to /portfolio controller
        where a binary semaphore will ensure atomic access while
        invoice is inserted
        */
        postData('/generate-invoice', data).then(() => {
            window.location = "/success/created";
            this.disabled = false;
        })
    })
});
