package co.smartreceipts.android.model;

import android.content.Context;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.io.File;
import java.sql.Date;
import java.util.TimeZone;

public interface Receipt extends Parcelable, Priceable {

    public static final String PARCEL_KEY = Receipt.class.getName();

    /**
     * Gets the primary key id for this receipt
     *
     * @return the receipt's autoincrement id
     */
    int getId();

    /**
     * Gets the parent trip for this receipt. This can only be null if it's detached from a {@link co.smartreceipts.android.model.Trip}
     * (e.g. if it's a converted distance).
     *
     * @return - the parent {@link co.smartreceipts.android.model.Trip}
     */
    Trip getTrip();

    /**
     * Gets the payment method associated with this receipt item. This may be {@code null}
     * if there is no associated payment method.
     *
     * @return the {@link co.smartreceipts.android.model.PaymentMethod} associated with this receipt item or {@code null} if
     * there is none.
     */
    PaymentMethod getPaymentMethod();

    /**
     * Checks if a payment method is attached to this receipt and can be retrieved via {@link #getPaymentMethod()}.
     *
     * @return {@code true} if there is a {@link co.smartreceipts.android.model.PaymentMethod}, {@code false} otherwise.
     */
    boolean hasPaymentMethod();

    /**
     * Gets the name of this receipt. This should never be {@code null}.
     *
     * @return the name of this receipt as a {@link java.lang.String}.
     */
    @NonNull
    String getName();

    /**
     * Checks if this receipt is connected with a file (e.g. a PDF or Image file)
     *
     * @return {@code true} if it has a file, {@code false} otherwise
     */
    boolean hasFile();

    /**
     * Checks if this receipt is connected to an image file
     *
     * @return {@code true} if it has an image file, {@code false} otherwise
     */
    boolean hasImage();

    /**
     * Checks if this receipt is connected to an PDF file
     *
     * @return {@code true} if it has a PDF file, {@code false} otherwise
     */
    boolean hasPDF();

    /**
     * Returns the data-type marker for the underlying file (e.g. "pdf" for PDF, "image" for image, or empty
     * if there is no file)
     *
     * @param context the current {@link android.content.Context}
     * @return the {@link java.lang.String} representation of the file-type
     */
    String getMarkerAsString(Context context);

    /**
     * Gets the Image attached to this receipt. This is identical to calling {@link #getFile()}
     *
     * @return the {@link java.io.File} or {@code null} if none is present
     */
    File getImage();

    /**
     * Gets the PDF attached to this receipt. This is identical to calling {@link #getFile()}
     *
     * @return the PDF {@link java.io.File} or {@code null} if none is present
     */
    File getPDF();

    /**
     * Gets the file attached to this receipt.
     *
     * @return the Image {@link java.io.File} or {@code null} if none is present
     */
    File getFile();

    /**
     * Gets the absolute path of this Receipt's file from {@link #getFile()}.
     *
     * @return a representation of the file path via {@link #getFile()} and {@link java.io.File#getAbsolutePath()}.
     */
    String getFilePath();

    /**
     * Gets the name of this Receipt's file from {@link #getFile()}.
     *
     * @return a representation of the file name via {@link #getFile()} and {@link java.io.File#getName()}.
     */
    String getFileName();

    /**
     * Attaches a file to this receipt (e.g. a PDF or Image)
     *
     * @param file the {@link java.io.File} to associate with this receipt or {@code null} if the picture was removed
     */
    void setFile(File file);

    /**
     * Gets the source from which this receipt was built for debugging purposes
     *
     * @return the {@link co.smartreceipts.android.model.Source}
     */
    Source getSource();

    /**
     * Gets the category to which this receipt is attached
     *
     * @return the {@link java.lang.String} representation of the category
     */
    String getCategory();

    /**
     * Gets the user defined comment for this receipt
     *
     * @return - the current comment as a {@link java.lang.String}
     */
    String getComment();


    /**
     * Gets the tax associated with this receipt
     *
     * @return the {@link co.smartreceipts.android.model.Price} for the tax
     */
    Price getTax();


    /**
     * Returns the date during which this receipt was taken
     *
     * @return the {@link java.sql.Date} this receipt was captured
     */
    Date getDate();

    /**
     * Gets a formatted version of the date based on the timezone and locale for a given separator. In the US,
     * we might expect to see a result like "10/23/2014" returned if we set the separator as "/"
     *
     * @param context   - the current {@link android.content.Context}
     * @param separator - the date separator (e.g. "/", "-", ".")
     * @return the formatted date string for this receipt
     */
    String getFormattedDate(Context context, String separator);

    /**
     * Gets the time zone in which the date was set
     *
     * @return - the {@link java.util.TimeZone} for the date
     */
    TimeZone getTimeZone();

    /**
     * Checks if the receipt was marked as expensable (i.e. counting towards the total) or not
     *
     * @return {@code true} if it's expensable, {@code false} otherwise
     */
    boolean isExpensable();

    /**
     * Checks if this receipt should be printed as a full page in the PDF report
     *
     * @return {@code true} if it's printed as a full page, {@code false} otherwise
     */
    boolean isFullPage();

    /**
     * Checks if this receipt is currently selected or not
     *
     * @return {@code true} if it's currently selected. {@code false} otherwise
     */
    boolean isSelected();

    /**
     * Returns the "index" of this receipt relative to others. If this was the second earliest receipt, it would appear
     * as a receipt of index 2.
     *
     * @return the index of this receipt
     */
    int getIndex();

    /**
     * Returns the user defined string for the 1st "extra" field
     *
     * @return the {@link java.lang.String} for the 1st custom field or {@code null} if not set
     */
    String getExtraEditText1();

    /**
     * Returns the user defined string for the 2nd "extra" field
     *
     * @return the {@link java.lang.String} for the 2nd custom field or {@code null} if not set
     */
    String getExtraEditText2();

    /**
     * Returns the user defined string for the 3rd "extra" field
     *
     * @return the {@link java.lang.String} for the 3rd custom field or {@code null} if not set
     */
    String getExtraEditText3();

    /**
     * Checks if we have a 1st "extra" field
     *
     * @return {@code true} if we have a 1st "extra" field or {@code false} if not
     */
    boolean hasExtraEditText1();

    /**
     * Checks if we have a 2nd "extra" field
     *
     * @return {@code true} if we have a 2nd "extra" field or {@code false} if not
     */
    boolean hasExtraEditText2();

    /**
     * Checks if we have a 3rd "extra" field
     *
     * @return {@code true} if we have a 3rd "extra" field or {@code false} if not
     */
    boolean hasExtraEditText3();

}