class MobileNavbar{
    constructor(){
        this.mobileMenu = document.querySelector(mobileMenu);
        this.navList = document.querySelector(navList);
        this.navLinks = document.querySelectorAll(navlinks);
        this.activeClass = "active";

        this.handleClick = this.handleClick.bind(this);
    }

    animateLinks(){
        this.navLinks.forEach((link, index) => {
            console.log(index);
        link.style.animation
        ? (link.style.animation = "")
        : (link.style.animation = 'navLinkFade 0.5 case forwards 0.3s');
        
        });
    }
    

    handleClick() {
        this.animateLinks();
        this.navList.classList.toggle(this.activeClass);
    }


    addClickEvent() {
        this.mobileMenu.addEventListener("click", this.handleClick);
    }

    init() {
        if(this.mobileMenu) {
            this.addClickEvent();
        }
        return this;
    }
}

const mobileNavbar = new MobileNavbar(
    ".mobile-menu",
    ".nav-list",
    ".nav-list li",
);
mobileNavbar.init();