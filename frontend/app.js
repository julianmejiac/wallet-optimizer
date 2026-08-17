const API_URL = "http://localhost:8080";

const loadCardsButton =
    document.getElementById("loadCardsButton");

const cardsContainer =
    document.getElementById("cardsContainer");

const addCardForm =
    document.getElementById("addCardForm");

const recommendationForm =
    document.getElementById("recommendationForm");

const recommendationContainer =
    document.getElementById("recommendationContainer");


loadCardsButton.addEventListener("click", loadCards);

//loadCards function
async function loadCards() {
    const response = await fetch(`${API_URL}/cards`);
    const cards = await response.json();

    cardsContainer.innerHTML = "";

    for (const card of cards) {
        const cardDiv = document.createElement("div");
        cardDiv.classList.add("card");

        cardDiv.innerHTML = `
            <h3>${card.name}</h3>
            <p>Issuer: ${card.issuer}</p>
            <p>Network: ${card.network}</p>
            <p>Annual Fee: $${card.annualFee}</p>

            <div class="cardButtons">
                <button class="toggleRewardsButton">
                    View Rewards
                </button>

                <button class="editCardButton">
                    Edit
                </button>

                <button class="deleteCardButton">
                    Delete
                </button>
            </div>

            <div class="rewardSection hidden">
                <div class="rewardList"></div>

                <h4>Add Reward Rule</h4>

                <form class="rewardForm">
                    <input
                        type="text"
                        class="rewardCategory"
                        placeholder="Category"
                        required
                    >

                    <input
                        type="number"
                        class="rewardCashback"
                        placeholder="Cashback %"
                        min="0.01"
                        step="0.01"
                        required
                    >

                    <button type="submit">
                        Add Reward
                    </button>
                </form>
            </div>
        `;

        cardsContainer.appendChild(cardDiv);

        const toggleButton =
            cardDiv.querySelector(".toggleRewardsButton");

        const editCardButton =
            cardDiv.querySelector(".editCardButton");

        const deleteCardButton =
            cardDiv.querySelector(".deleteCardButton");
        const rewardSection =
            cardDiv.querySelector(".rewardSection");

        const rewardList =
            cardDiv.querySelector(".rewardList");

        const rewardForm =
            cardDiv.querySelector(".rewardForm");
        // What happens when click on the RewardsButton
        toggleButton.addEventListener("click", async function () {
            rewardSection.classList.toggle("hidden");

            if (!rewardSection.classList.contains("hidden")) {
                await loadRewardRules(card.id, rewardList);
            }
        });
        // What happens when click on the editCardButton
        editCardButton.addEventListener("click", ()=> handleEditCard (card, cardDiv))

        // What happens when click on the deleteCardButton

        deleteCardButton.addEventListener("click", ()=> handleDeleteCard(card,cardDiv))

        // What happens when submitting the rewardForm
        rewardForm.addEventListener("submit", (event)=> handleAddReward(event,card,rewardList,rewardForm));
    }
}


//handleDeleteCard
async function handleDeleteCard(card) {


        const response= await fetch(
        `${API_URL}/cards/${card.id}`,
        {method: "DELETE",
                }
      );

        if (response.ok){
        await loadCards();
        }
        else{
        alert("Delete was unsuccessful");
        }




}

//handleEditCard
function handleEditCard(card, cardDiv) {

    const existingEditSection =
        cardDiv.querySelector(".editSection");

    if (existingEditSection) {
        existingEditSection.remove();
        return;
    }
const editSection= document.createElement("div")
editSection.classList.add("editSection")
editSection.innerHTML=`
            <form class="cardEditForm">
            <input
                    type="text"
                    class="editName"
                    value="${card.name}"
                    required
                >
            <input
                        type="text"
                        class="editIssuer"
                        value="${card.issuer}"
                        required
                            >
            <input
                        type="text"
                        class="editNetwork"
                        value="${card.network}"
                        required
                            >
            <input
                        type="number"
                        class="editAnnualFee"
                        value="${card.annualFee}"
                        min="0"
                        step="0.01"
                        required
                            >
            <label>
                Active
                <input
                    type="checkbox"
                    class="editActive"
                    ${card.active ? "checked":""}
                >
            </label>
            <button type="submit" class="saveCardButton" >
            Save Changes
            </button>
            </form>` ;

 const editForm =editSection.querySelector(".cardEditForm");

 editForm.addEventListener(
                "submit",
                (event) => saveCardChanges(event, card)
            );

            cardDiv.appendChild(editSection);
}
// Saving Changes of an edited Card
async function saveCardChanges(event, card){
event.preventDefault();
//read inputs
const editForm=event.currentTarget
const name=editForm.querySelector(".editName").value;
const issuer=editForm.querySelector(".editIssuer").value;
const network=editForm.querySelector(".editNetwork").value;
const annualFee=Number(editForm.querySelector(".editAnnualFee").value);
const active = editForm.querySelector(".editActive").checked;

const cardUpdateRequest = {
    name: name,
    issuer: issuer,
    network: network,
    annualFee: annualFee,
    active: active
};
//put requests
const response = await fetch(
    `${API_URL}/cards/${card.id}`,
    {
        method: "PUT",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(cardUpdateRequest)
    }
);

if(response.ok){
await loadCards();}
else{ console.log("Could not update card");}


}

//loadRewardRules function
async function loadRewardRules(cardId, rewardList) {
    const response =
        await fetch(
            `${API_URL}/cards/${cardId}/reward-rules`
        );

    const rewards =
        await response.json();

    rewardList.innerHTML = "";

    if (rewards.length === 0) {
        rewardList.innerHTML =
            "<p>No reward rules yet.</p>";

        return;
    }

    for (const reward of rewards) {
        const rewardDiv =
            document.createElement("div");

        rewardDiv.classList.add("reward");

        rewardDiv.innerHTML = `
            <p>
                ${reward.category}:
                ${reward.cashbackPercent}%
            </p>
            <button class="editRewardButton">
                    Edit
                </button>
        `;
        const editRewardButton =
            rewardDiv.querySelector(".editRewardButton");

        editRewardButton.addEventListener(
            "click",
            () => showEditRewardForm( cardId, reward, rewardDiv, rewardList)
        );

        rewardList.appendChild(rewardDiv);
    }
}
//Adding a Card
addCardForm.addEventListener(
    "submit",
    handleAddCard
);

//Recommending a card
recommendationForm.addEventListener(
    "submit",
    handleRecommendation
);


async function handleAddCard(event)
{

        event.preventDefault();

        const cardRequest = {
            name:
                document.getElementById("name").value,

            issuer:
                document.getElementById("issuer").value,

            network:
                document.getElementById("network").value,

            annualFee:
                Number(
                    document.getElementById("annualFee").value
                )
        };

        const response =
            await fetch(`${API_URL}/cards`, {

                method: "POST",

                headers: {
                    "Content-Type": "application/json"
                },

                body: JSON.stringify(cardRequest)
            });

        if (response.ok) {

            addCardForm.reset();

            await loadCards();

        } else {

            const errorBody = await response.json();
                alert(errorBody.error);
        }
    }

// Showing recommendation

async function handleRecommendation(event){
        event.preventDefault();

        const category =
            document.getElementById("category").value;

        const response =
            await fetch(
                `${API_URL}/recommend?category=${encodeURIComponent(category)}`
            );

        const recommendations =
            await response.json();

        recommendationContainer.innerHTML = "";

        if (recommendations.length === 0) {

            recommendationContainer.innerHTML =
                "<p>No recommendation found.</p>";

            return;
        }

        for (const recommendation of recommendations) {

            const recommendationDiv =
                document.createElement("div");

            recommendationDiv.classList.add(
                "recommendation"
            );

            recommendationDiv.innerHTML = `
                <h3>${recommendation.cardName}</h3>
                <p>
                    Cashback:
                    ${recommendation.cashbackPercent}%
                </p>
            `;

            recommendationContainer.appendChild(
                recommendationDiv
            );
        }
}
// Adding a Reward
async function handleAddReward (event, card, rewardList, rewardForm)
{
            event.preventDefault();

            const category =
                rewardForm.querySelector(".rewardCategory").value;

            const cashbackPercent =
                Number(
                    rewardForm.querySelector(".rewardCashback").value
                );

            const rewardRequest = {
                category: category,
                cashbackPercent: cashbackPercent
            };

            const response =
                await fetch(
                    `${API_URL}/cards/${card.id}/reward-rules`,
                    {
                        method: "POST",

                        headers: {
                            "Content-Type": "application/json"
                        },

                        body: JSON.stringify(rewardRequest)
                    }
                );

            if (response.ok) {
                rewardForm.reset();

                await loadRewardRules(
                    card.id,
                    rewardList
                );
            } else {
                alert("Could not add reward rule.");
            }
        }

//showEditRewardForm
function showEditRewardForm(
    cardId,
    reward,
    rewardDiv,
    rewardList
) {
    const existingEditSection =
        rewardDiv.querySelector(".editRewardSection");

    if (existingEditSection) {
        existingEditSection.remove();
        return;
    }

    const editSection =
        document.createElement("div");

    editSection.classList.add("editRewardSection");

    editSection.innerHTML = `
        <form class="editRewardForm">

            <input
                type="text"
                class="editRewardCategory"
                value="${reward.category}"
                required
            >

            <input
                type="number"
                class="editRewardCashback"
                value="${reward.cashbackPercent}"
                min="0.01"
                step="0.01"
                required
            >

            <button type="submit">
                Save Changes
            </button>

        </form>
    `;

    const editForm =
        editSection.querySelector(".editRewardForm");

    editForm.addEventListener(
        "submit",
        (event) =>
            saveRewardChanges(
                event,
                cardId,
                reward,
                rewardList
            )
    );

    rewardDiv.appendChild(editSection);
}
//SaveRewardChanges
async function saveRewardChanges(
    event,
    cardId,
    reward,
    rewardList
) {
    event.preventDefault();

    const editForm = event.currentTarget;

    const category =
        editForm.querySelector(
            ".editRewardCategory"
        ).value;

    const cashbackPercent =
        Number(
            editForm.querySelector(
                ".editRewardCashback"
            ).value
        );

    const rewardRequest = {
        category,
        cashbackPercent
    };

    const response = await fetch(
        `${API_URL}/cards/${cardId}/reward-rules/${reward.id}`,
        {
            method: "PUT",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(rewardRequest)
        }
    );

    if (response.ok) {
        await loadRewardRules(cardId, rewardList);
    } else {
        console.log("Could not update reward rule");
    }
}