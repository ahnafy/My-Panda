import {TestBed, ComponentFixture, async} from '@angular/core/testing';
import {HomeComponent} from './home.component';
import {CustomModule} from '../custom.module';
import {MATERIAL_COMPATIBILITY_MODE} from '@angular/material';
import {Observable} from "rxjs/Observable";
import {Emotion} from "./emotion";
import {EmotionService} from "./home.service";

describe('Home', () => {

    let Hcomponent: HomeComponent;
    let fixture: ComponentFixture<HomeComponent>;

    let homeServiceStub: {
        getHome: () => Observable<Emotion[]>
    };

    beforeEach(() => {
        homeServiceStub = {
            getHome: () => Observable.of ([
                {
                    _id: 'test1',
                    userID: 'tester1',
                    mood: 'happy',
                    date: 'Sat Apr 07 2018 15:23:28 GMT-0000 (UTC)',
                    intensity: 3,
                    description: 'im happy',
                },
                {
                    _id: 'test2',
                    userID: 'tester2',
                    mood: 'anxious',
                    date: 'Sat Apr 07 2018 15:23:28 GMT-0000 (UTC)',
                    intensity: 5,
                    description: 'im super anxious',
                },
                {
                    _id: 'test3',
                    userID: 'tester3',
                    mood: 'meh',
                    date: 'Sat Apr 07 2018 15:23:28 GMT-0000 (UTC)',
                    intensity: 1,
                    description: 'im meh',
                }
            ])
        };

        TestBed.configureTestingModule({
            imports: [CustomModule],
            declarations: [HomeComponent], // declare the test component
            providers: [{provide: EmotionService, useValue: homeServiceStub},
                {provide: MATERIAL_COMPATIBILITY_MODE, useValue: true}]
        });
    });

    beforeEach(async(() => {
        TestBed.compileComponents().then(() => {
            fixture = TestBed.createComponent(HomeComponent);
            Hcomponent = fixture.componentInstance;
            fixture.detectChanges();

            Hcomponent.selectedEmotion = 'happy';
            Hcomponent.emojiRating = 3;
        });
    }));

    it('should reset emotion', () => {
        Hcomponent.emojiRating = 1;
        Hcomponent.resetPage();
        expect(Hcomponent.emojiRating).toBe(0);
    });

    it('should set emotion to sad', () => {
        Hcomponent.selectedEmotion = 'happy';
        Hcomponent.selectEmotion('sad');
        expect(Hcomponent.selectedEmotion).toBe('sad');
    });

    it('should set emotion to meh', () => {
        Hcomponent.selectedEmotion = 'happy';
        Hcomponent.selectEmotion('meh');
        expect(Hcomponent.selectedEmotion).toBe('meh');
    });

    it('should return false', () => {
        Hcomponent.selectedEmotion = 'anxious';
        let tempBoolean: boolean = Hcomponent.showNextButton();
        expect(tempBoolean == false).toBeTruthy();
    });

    it('should return false', () => {
        Hcomponent.selectedEmotion = 'none';
        let tempBoolean: boolean = Hcomponent.showNextButton();
        expect(tempBoolean == false).toBeTruthy();
    });

    it('should return false', () => {
        Hcomponent.emojiRating = 4;
        let tempBoolean: boolean = Hcomponent.showNextButton();
        expect(tempBoolean == false).toBeTruthy();
    });

    it('should return false', () => {
        Hcomponent.emojiRating = 0;
        let tempBoolean: boolean = Hcomponent.showNextButton();
        expect(tempBoolean == false).toBeTruthy();
    });

    it('should return false', () => {
        Hcomponent.emojiRating = 0;
        let tempBoolean: boolean = Hcomponent.showNextButton();
        expect(tempBoolean).toBe(false);
    });

    it('should return false', () => {
        Hcomponent.emojiRating = 2;
        let tempBoolean: boolean = Hcomponent.showNextButton();
        expect(tempBoolean).toBe(false);
    });
});

