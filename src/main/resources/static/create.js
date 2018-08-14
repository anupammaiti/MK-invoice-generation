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
    const existingInvoices = $('#invoiceNumberList').val().split(',');

    jQuery.validator.addMethod("duplicateChecker", function(value, element) {
        let invoiceNumArray = existingInvoices;
        let isLegalNumber = true;

        invoiceNumArray.forEach((number)=>{
            if(value===number) isLegalNumber = false;
        });

        return this.optional(element) || isLegalNumber;
    }, "* This invoice number <b><u>already exists</u></b> in the database.");

    //Set a range of characters for the user input to have
    jQuery.validator.addMethod("charRange", function(value, element, options) {
        let min = options[1];
        let max = options[2];

        return this.optional(element) || (value.length >= min && value.length <= max);
    }, "* Minimum length is {1} and maximum is {2}");

    //Check for floating point number
    jQuery.validator.addMethod("checkFloat", function(value, element) {

        return this.optional(element) || (value.includes('.'));
    }, "* This has to be a floating point number (Leave empty to ignore). <i>e.g. 0.20 or 150.0, etc..</i>");

    //Set a limit for decimal points
    jQuery.validator.addMethod("floatingLimit", function(value, element, options) {
        let rightSide = value.split('.')[1];

        return this.optional(element) || (rightSide.length <= options[1] || value.length < 1);
    }, "* There can only be <u><b>{1}</b></u> digits after the decimal point");

    //considering that user input will be used as float, se max limit of digits
    jQuery.validator.addMethod("digitLimit", function(value, element) {
        if(value.includes('.')){
            return this.optional(element) || ( value.length <= 8);
        }
        else
            return this.optional(element) || value.length <= 7;


    }, "* Please limit the number of digits you enter to a maximum of '<u><b>7</b></u>'.");

    //Disallow strange characters
    jQuery.validator.addMethod("specialChars", function( value, element ) {
        let regex = new RegExp("^[a-zA-Z0-9_#\-£$€]");

        return this.optional(element) || regex.test(value);
    },  "* Letters, numbers, dashes, underscores and ('#', '£', '$', '€') only");

    jQuery.validator.addMethod("checkYear", function( value, element, options ) {
        let year = parseInt(value);

        return this.optional(element) || year>=options[1] && year<=options[2] && value.length === 4;
    },  "* Year must be in the format '<u>YYYY</u>' and can not be less than {1} or more than {2}");

    jQuery.validator.addMethod(
        "customDateISO",
        $.validator.methods.dateISO,
        "Please enter a valid date. <i>(Allowed formats -> <u>yyyy/mm/dd</u> or <u>yyyy-mm-dd</u>)</i>");

    /* example for changing messages of pre-existing methods:
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
                invoiceDate: {
                    customDateISO: true
                },
                invoiceNumber: {
                    specialChars: true,
                    charRange : [true, 1, 20], //min and max range of input
                    duplicateChecker: true
                },
                year: {
                    number: true,
                    checkYear: [true, 2007, (new Date()).getFullYear()]
                },
                manualVatRate: {
                    number: true, //check if input is number
                    checkFloat: true, //check if it is a floating point
                    digitLimit:true, //keep in floating point memory boundaries
                    floatingLimit: [true, 3] //check if decimal points are not more than 3
                },
                baseCharge: {
                    number: true,
                    digitLimit: true
                }
            }
        });


        if(manualVatRate.val()==='') {
            manualVatRate.rules('remove', 'required');
            vatRate.rules('add', {required: true});
            //console.log("manual vat-rate input required: FALSE");
        }
        if(manualVatRate.val()!=='') {
            vatRate.rules('remove', 'required');
            manualVatRate.rules('add', {required: true});
            //console.log("manual vat-rate input required: TRUE");
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
            vatRate: manualVatRate==='' ?parseFloat(document.getElementById('vat').value) :parseFloat(manualVatRate),
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

        this.disabled = false;
    })
});
