import { element, by, ElementFinder } from 'protractor';
import { waitUntilDisplayed, waitUntilHidden, isVisible } from '../../util/utils';

const expect = chai.expect;

export default class UtteranceUpdatePage {
  pageTitle: ElementFinder = element(by.id('voicebotApp.utterance.home.createOrEditLabel'));
  saveButton: ElementFinder = element(by.id('save-entity'));
  cancelButton: ElementFinder = element(by.id('cancel-save'));
  utteranceInput: ElementFinder = element(by.css('input#utterance-utterance'));
  intentSelect: ElementFinder = element(by.css('select#utterance-intent'));

  getPageTitle() {
    return this.pageTitle;
  }

  async setUtteranceInput(utterance) {
    await this.utteranceInput.sendKeys(utterance);
  }

  async getUtteranceInput() {
    return this.utteranceInput.getAttribute('value');
  }

  async intentSelectLastOption() {
    await this.intentSelect.all(by.tagName('option')).last().click();
  }

  async intentSelectOption(option) {
    await this.intentSelect.sendKeys(option);
  }

  getIntentSelect() {
    return this.intentSelect;
  }

  async getIntentSelectedOption() {
    return this.intentSelect.element(by.css('option:checked')).getText();
  }

  async save() {
    await this.saveButton.click();
  }

  async cancel() {
    await this.cancelButton.click();
  }

  getSaveButton() {
    return this.saveButton;
  }

  async enterData() {
    await waitUntilDisplayed(this.saveButton);
    await this.setUtteranceInput('utterance');
    await this.intentSelectLastOption();
    await this.save();
    await waitUntilHidden(this.saveButton);
  }
}
