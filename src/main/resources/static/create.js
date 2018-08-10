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
 * Will send object holding values needed for an invoice back, as JSON.
 * for validation library documentation, see https://jqueryvalidation.org/documentation/
 */
document.addEventListener('DOMContentLoaded', function () {

    jQuery.validator.addMethod("characterRange", function(value, element, options) {
        let min = options[1];
        let max = options[2];
        return this.optional(element) || (value.length >= min && value.length <= max);
    }, "* Minimum length is {1} and maximum is {2}");

    jQuery.validator.addMethod("charRangeVat", function(value, element, options) {
        let min = options[1];
        let max = options[2];

        return this.optional(element) || (value.length >= min && value.length <= max || value.length < 1);
    }, "* Minimum length is {1} and maximum is {2}. Leave empty to ignore");

    jQuery.validator.addMethod("checkDecimalPoint", function(value, element, options) {
        let userInput = options[1];
    });

    /* example:
    jQuery.validator.addMethod("myRule", $.validator.methods.required, "BLLLLLLLLLLLLLLLLL");
    * usage:
    rules:{ #element: {myRule:true}}
    */

    document.getElementById('submitButton').addEventListener('click', function () {
        this.disabled = true;
        let form = $("#myForm");
        let manualVatRate = $('#manualVatRate');
        let vatRate = $('#vat');

        $(form).validate({
            rules : {
                invoiceNumber : {characterRange : [true, 5, 20] },
                manualVatRate : {charRangeVat : [true, 3, 5]}
            }
        });
        if(manualVatRate.val()==='') {
            manualVatRate.rules('remove', 'required');
            vatRate.rules('add', {required: true});
        }
        else if(manualVatRate.val()!=='') {
            vatRate.rules('remove', 'required');
            manualVatRate.rules('add', {required: true});
        }

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
            vatRate: manualVatRate === '' ? document.getElementById('vat').value : manualVatRate,
            serviceProvided: parseInt(document.getElementById('serviceProvided').value),
            bankAccount: parseInt(document.getElementById('bankAccount').value),
            fromCurrency: parseInt(document.getElementById('fromCurrency').value),
            toCurrency: parseInt(document.getElementById('toCurrency').value),
            baseCharge: parseFloat(document.getElementById('baseCharge').value)
        };

        //If form is valid, send post http request
        if(form.valid()){
            form.submit();
            postData('/generate-invoice', data);
        }

        console.log('Form is valid: '+form.valid());
        this.disabled = false;
    })
});
