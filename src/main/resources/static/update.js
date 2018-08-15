/**
 *
 * @author Petros Soutzis
 */

 // @see "https://github.com/soutzis/MK-invoice-generation
 // /blob/cbf6b3f12d86fa885ba5b8dd972fe0f49c0c1a1d/src/main/resources/static/update.js"
 // repository/update.js</a> for the version of update.js, where it used openexchangerate to
 // automatically update exchange rate on client-side.


const checkboxKeyword = 'Box';
const manualInputKeyword = 'Manual';
const textInputKeyword = 'Text';
const groupedValuesKeyword = 'Group';
const divKeyword = 'Div';

/**
 * Will send an object passed as parameter, as a POST http request to the specified url.
 * @param url The url to send an http request to.
 * @param data The object to send with the request.
 * @return {Promise<string | never | void>}
 */
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
 *
 * @param array Is the array of elements to use to extract data.
 * @param data Is the object to store data to.
 * @param doc The document that elements reside at.
 */
function setValuesToObj(array = [], data={}, doc){
    array.forEach(function(arrayElement){
        let elementId = arrayElement.id;
        data[elementId] = doc.getElementById(elementId).value;
    });

    return data;
}

/**
 * Function will fetch elements that contain a 'keyword' in their id
 * and add them to an object passed as parameter. Elements with this keyword are
 * elements elements that accept both text input from the user and a selection from a drop down list.
 * If a text input is set, the function will use that value to populate the object, otherwise
 * If it is not set, the value from the dropdown list will be used.
 * @param array An array of elements to get their value.
 * @param data An object to be populated with the fetched elements' values.
 * @param doc The HTML document that contains the elements.
 * @param keyword
 * @return data An object containing the values of all elements whose id contain the keyword parameter
 */
function setManualValuesToObj(array=[], data={}, doc, keyword){
    array.forEach(function(arrayElement){
        let manualElementId = arrayElement.id;
        let elementId = manualElementId.slice(0,-(keyword.length));
        let manualInput = doc.getElementById(manualElementId).value;
        let input = doc.getElementById(elementId).value;

        data[elementId] = manualInput==='' ? data[elementId] = input : data[elementId] = manualInput;
    });

    return data;
}

/**
 * Function to add functionality to checkboxes. If a checkbox is checked, it will let the user set a new
 * value for the corresponding element
 * @param checkboxes An array of all the checkboxes on the document
 */
function setCheckboxFunctionality(checkboxes=[]){
    for(let i=0; i<checkboxes.length; i++){
        //This conditional is entered only if the checkbox will trigger a "set" of inputs
        if((checkboxes[i].id).endsWith(groupedValuesKeyword))
        {
            let divId = (checkboxes[i].id).replace(groupedValuesKeyword, divKeyword);
            let divElement = document.getElementById(divId);
            let groupValues = $('#'+divId +' :input').get();

            let groupElements = [];
            for(let k=0; k<groupValues.length; k++){
                let obj = {
                    id: (groupValues[k]).id,
                    defaultValue: (groupValues[k]).value
                };
                groupElements.push(obj);
            }
            checkboxes[i].onclick = function () {
                if(checkboxes[i].checked){
                    divElement.style.display = 'block';
                }
                else {
                    divElement.style.display = 'none';
                    groupElements.forEach((obj) =>{
                        (document.getElementById(obj.id)).value = obj.defaultValue;
                    });
                }
            }
        }

        else if((checkboxes[i].id).endsWith(textInputKeyword)){
            let elementId = (checkboxes[i].id).slice(0,-(textInputKeyword.length)); //e.g. invoiceNumber
            let divId = elementId+divKeyword; //e.g. invoiceNumberDiv
            let element = document.getElementById(elementId);
            let defaultValue = element.value;

            checkboxes[i].onclick = function () {
                if(checkboxes[i].checked){
                    element.value = '';
                    document.getElementById(divId).style.display = 'block';
                }
                else {
                    document.getElementById(divId).style.display = 'none';
                    element.value = defaultValue;
                }
            }
        }
        //This conditional will be entered if the checkbox will trigger 1 input, manual or otherwise
        //Checkbox id is the same as element Id, but has the string 'Box' to the end. e.g. 'portfolioBox'
        else {
            let elementId = (checkboxes[i].id).slice(0,-(checkboxKeyword.length)); //e.g. invoiceNumber
            let divId = elementId+divKeyword; //e.g. invoiceNumberDiv
            let manualId = elementId+manualInputKeyword;//e.g. invoiceNumberManual
            let element = document.getElementById(elementId);
            let defaultValue = element.value;

            checkboxes[i].onclick = function () {
                if(checkboxes[i].checked)
                    document.getElementById(divId).style.display = 'block';
                else {
                    document.getElementById(divId).style.display = 'none';
                    element.value = defaultValue;
                    if(document.getElementById(manualId)!== null)
                        document.getElementById(manualId).value = '';
                }
            }
        }
    }
}


/**
 * The checkbox corresponding to an element should have the element's id as its own id, but with
 * the word box attached to it. e.g. element-> id="client" & checkbox-> id="clientBox"
 * Id of element is retrieved by removing the string 'Box' from the Id of current checkbox.
 * IF user checks box, they can edit value of corresponding element. If they uncheck the box,
 * element will be assigned its initial value.
 *
 */
document.addEventListener('DOMContentLoaded', function () {
    const checkboxes = document.getElementsByName('editBox');
    const defaultFromCurrency = document.getElementById('fromCurrency').value;
    const defaultToCurrency = document.getElementById('toCurrency').value;
    const defaultExchangeRate = document.getElementById('exchangeRate').value;
    const companyId = document.getElementById('companyId').value;

    //invoice number of existing records
    const existingInvoices = $('#invoiceNumberList').val().split(',');

    setCheckboxFunctionality(checkboxes);

    //Checks if user entered invoice number that already exists
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

    //Disallow strange characters, but allow #, £, $, €, _
    jQuery.validator.addMethod("specialChars", function( value, element ) {
        let regex = new RegExp("^[a-zA-Z0-9_#\-£$€]");

        return this.optional(element) || regex.test(value);
    },  "* Letters, numbers, dashes, underscores and ('#', '£', '$', '€') only");

    //Disallow strange characters
    jQuery.validator.addMethod("alphanumericOnly", function( value, element ) {
        let regex = new RegExp("^[a-zA-Z0-9\-\.]");

        return this.optional(element) || regex.test(value);
    },  "* Enter only alphanumeric characters");

    //allow only valid ISO date format inputs
    jQuery.validator.addMethod(
        "customDateISO",
        $.validator.methods.dateISO,
        "Please enter a valid date. <i>(See allowed formats above)</i>.");

    //allow only valid 'YEAR' values
    jQuery.validator.addMethod("checkYear", function( value, element, options ) {
        let year = parseInt(value);

        return this.optional(element) || year>=options[1] && year<=options[2] && value.length === 4;
    },  "* Year must be in the format '<u>YYYY</u>' and can not be less than {1} or more than {2}");

    jQuery.validator.addMethod("exchangeRateNote", $.validator.methods.required,
        "* This field is required. See the <b>note</b> below.");

    //Script will set value of variable as null, if it is equal to an empty string
    document.getElementById('updateButton').addEventListener('click', function () {
        let myData = {};
        let form = $("#updateForm");
        let invoiceNo = $("#invoiceNumber").attr("name");
        let invoiceDate = $("#invoiceDate").attr("name");
        let year = $("#year").attr("name");
        let manualVatRateElement = $("#vatRateManual");
        let vatRateElement = $("#vatRate");
        let manualVatRate = manualVatRateElement.attr("name");
        let exchangeRate = $("#exchangeRate").attr("name");
        let baseCharge = $("#custodyCharge").attr("name");
        let companyName = $("#companyName").attr("name");
        let companyAddress = $("#companyAddress").attr("name");
        let companyCity = $("#companyCity").attr("name");
        let companyPostcode = $("#companyPostcode").attr("name");
        let companyVatNumber = $("#companyVatNumber").attr("name");

        $(form).validate({
            rules : {
                [invoiceNo]:{
                    specialChars: true,
                    charRange: [true, 1, 20],
                    duplicateChecker: true
                },
                [invoiceDate]:{
                    customDateISO: true
                },
                [year]:{
                    number: true,
                    checkYear: [true, 2007, (new Date()).getFullYear()]
                },
                [manualVatRate]:{
                    number: true, //check if input is number
                    checkFloat: true, //check if it is a floating point
                    digitLimit: true,
                    floatingLimit: [true, 3] //check if decimal points are not more than 3
                },
                [exchangeRate]:{
                    exchangeRateNote: true,
                    number: true,
                    digitLimit: true
                },
                [baseCharge]:{
                    number: true,
                    digitLimit: true
                },
                [companyName]:{
                    charRange: [true,1,50]
                },
                [companyAddress]:{
                    alphanumericOnly: true,
                    charRange: [true, 6, 99]
                },
                [companyCity]:{
                    alphanumericOnly: true,
                    charRange: [true,2,50]
                },
                [companyPostcode]:{
                    alphanumericOnly: true,
                    charRange: [true,4,20]
                },
                [companyVatNumber]:{
                    alphanumericOnly: true,
                    charRange: [true, 3, 20]
                }
            }
        });

        //Toggle required between vatRate and manualVatRate. Only one of them should be required.
        if(manualVatRateElement.val()!=='') {
            vatRateElement.rules('remove', 'required');
            manualVatRateElement.rules('add', {required: true});
            console.log("manual vat-rate input required: TRUE");
        }
        if(manualVatRateElement.val()==='') {
            manualVatRateElement.rules('remove', 'required');
            vatRateElement.rules('add', {required: true});
            console.log("manual vat-rate input required: FALSE");
        }

        //Get collection of elements based on the keyword that their name starts with.
        let $newValueCollection = $(":input[name^='newValue']");
        let $groupValueCollection = $(":input[name^='groupValue']");
        let $manualValueCollection = $(":input[name^='manualValue']");

        //Convert collections to arrays
        const newValues = $newValueCollection.get();
        const groupValues = $groupValueCollection.get();
        const manualValues = $manualValueCollection.get();

        //Dynamically populate the 'myData' object
        myData = setValuesToObj(newValues,myData,document);
        myData = setValuesToObj(groupValues,myData,document);
        myData = setManualValuesToObj(manualValues,myData,document, manualInputKeyword);

        //if user changed a currency, but left exchange rate as-is, then send null to calculate automatically
        if (myData.exchangeRate === defaultExchangeRate &&
            (myData.fromCurrency !== defaultFromCurrency || myData.toCurrency !== defaultToCurrency))
            myData.exchangeRate = null;

        //Always get hidden ids of invoice and company
        myData.invoiceId = parseInt(document.getElementById('invoiceId').value);
        myData.companyId = companyId === '' ? null : parseInt(companyId);
        //Convert vatRate to a Float.
        myData.vatRate = parseFloat(myData.vatRate);
        console.log(myData);

        if(form.valid()){
            form.submit();
            postData('/find/update/execute', myData);
        }
    });
});