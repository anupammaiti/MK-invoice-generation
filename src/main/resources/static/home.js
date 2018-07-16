
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

//Will send value of objects back as json
document.addEventListener('DOMContentLoaded', function () {
    document.getElementById('submitBtn').addEventListener('click', function () {
        let invoiceData = {
            invoiceType: document.getElementById('invoiceType').value,
            invoiceDate: new Date(document.getElementById('invoiceDate').value),
            invoiceNumber: document.getElementById('invoiceNumber').value,
            vatExempt: document.getElementById('vatExempt').value,
            reverseCharge: document.getElementById('reverseCharge').value,
            frequency: document.getElementById('frequency').value,
            period: document.getElementById('period').value,
            year: parseInt(document.getElementById('year').value),
        };

        let custodyChargeData = {
            chargeExcludingVat: document.getElementById('chargeExcludingVat').value,
            vatCharge: document.getElementById('vatCharge').value,
            chargeIncludingVat: document.getElementById('chargeIncludingVat').value
        };

        let vatData = {
            isApplicable:document.getElementById('isApplicable').value,
            vatId: document.getElementById('vatId').value,
            //vatRate: document.getElementById('vatRate').value
        };

        let servicesData = {
            serviceName: document.getElementById('service').value
        }

        let currencyData = {
            currencyCode: document.getElementById('currency').value
        }

        let currencyRateData = {
            currencyRateId: document.getElementById('currencyRateId').value
        }

        let portfolioCode = {
            portfolioCode: document.getElementById('portfolio').value
        }

        let bankAccountData = {
            id: document.getElementById('bankAccount').value
        }

        postData('/serviceProvided', servicesData)
        postData('/currency', currencyData)
        postData('/currencyRate', currencyRateData)
        postData('/portfolio', portfolioCode)
        postData('/bankAccount', bankAccountData)
        postData('/vat', vatData).then(() =>
        {
            postData('/invoice', invoiceData).then(() =>
            {
                postData('/fee', custodyChargeData)
            })
        })
        //window.location = "/success";
    })
})
