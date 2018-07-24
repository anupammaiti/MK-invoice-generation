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

const getData = (url = ``) => {
    // Default options are marked with *
    return fetch(url, {
        method: "GET", // *GET, POST, PUT, DELETE, etc.
        mode: "cors", // no-cors, cors, *same-origin
        cache: "no-cache", // *default, no-cache, reload, force-cache, only-if-cached
        credentials: "same-origin", // include, same-origin, *omit
        headers: {
            "Content-Type": "application/json; charset=utf-8",
            // "Content-Type": "application/x-www-form-urlencoded",
        },
        redirect: "follow", // manual, *follow, error
        referrer: "no-referrer", // no-referrer, *client
    })
        .then(response => response.json()) // parses response to JSON
        .catch(error => console.error(`Fetch Error =\n`, error));
};

/*POST REQUEST TO THE 'UPDATE' INVOICE CONTROLLER
    * The checkbox corresponding to an element should have the element's id as its own id, but with
    * the word box attached to it. e.g. element-> id="client" & checkbox-> id="clientBox"
    * Id of element is retrieved by removing the string 'Box' from the Id of current checkbox.
    * IF user checks box, they can edit value of corresponding element. If they uncheck the box,
    * element will be assigned its initial value.
    */
document.addEventListener('DOMContentLoaded', function () {
    const checkboxes = document.getElementsByName('editBox');
    for(let i=0; i<checkboxes.length; i++){
        let elementId = (checkboxes[i].id).slice(0,-3);
        let divId = elementId+'Div';
        let defaultValue = document.getElementById(elementId).value;
        checkboxes[i].onclick = function () {
            if(checkboxes[i].checked){
                document.getElementById(divId).style.display = 'block';
            }
            else {
                document.getElementById(divId).style.display = 'none';
                document.getElementById(elementId).value = defaultValue;
            }
        }
    }
    document.getElementById('updateButton').addEventListener('click', function () {
        this.disabled = true;
        const newValues = document.getElementsByName('newValue');
        let myData = {};
        for(let i=0; i<newValues.length; i++) {
            let elementId = newValues[i].id;
            myData[elementId] = document.getElementById(elementId).value
        }
        myData.id = document.getElementById('invoiceId').value;
        console.log(myData);

        postData('/find/update/execute', myData).then(() =>
        {
            window.location = '/success/updated';
        });

        this.disabled = false;

    });
});

        /*getData(`/invoice/${document.getElementById('selectedInvoice').value}`).then(response=>{
            document.getElementById("editPopup").style.display = 'block';
            document.getElementById('type').innerText = response.invoiceType;
            document.getElementById('clientName').innerText = response.client;
        })*/