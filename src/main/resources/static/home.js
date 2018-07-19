
console.log('js script')
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
 * TODO if(document.getElementById('<any variable>').value === "") var = null;
 */
document.addEventListener('DOMContentLoaded', function () {
    document.getElementById('submitBtn').addEventListener('click', function () {
        this.disabled = true;
        let invoiceData = {
            invoiceType: document.getElementById('invoiceType').value,
            invoiceDate: new Date(document.getElementById('invoiceDate').value),
            invoiceNumber: document.getElementById('invoiceNumber').value,
            vatExempt: document.getElementById('vatExempt').value,
            reverseCharge: document.getElementById('reverseCharge').value,
            frequency: document.getElementById('frequency').value,
            period: document.getElementById('period').value,
            year: parseInt(document.getElementById('year').value)};

        let exchangeRateData = {
            fromCurrencyId: document.getElementById('currency').value,
            toCurrencyId: document.getElementById('toCurrency').value};

        let custodyChargeData = {
            chargeExcludingVat: document.getElementById('chargeExcludingVat').value,
            vatRateId: document.getElementById('vatId').value};

        let vatData = {
            isApplicable:document.getElementById('isApplicable').value,
            vatId: document.getElementById('vatId').value};
        let servicesData = {id: document.getElementById('service').value};
        let currencyData = {currencyId: document.getElementById('currency').value};
        let portfolio = {id: document.getElementById('portfolio').value};
        let bankAccountData = {id: document.getElementById('bankAccount').value};

        postData('/exchangeRate', exchangeRateData);
        postData('/serviceProvided', servicesData);
        postData('/currency', currencyData);
        postData('/portfolio', portfolio);
        postData('/bankAccount', bankAccountData);
        postData('/fee', custodyChargeData);
        postData('/vat', vatData).then(() => {
            postData('/invoice', invoiceData).then(() => {
                window.location = "/success/created";
                this.disabled = false;
            })
        })
    })
});
